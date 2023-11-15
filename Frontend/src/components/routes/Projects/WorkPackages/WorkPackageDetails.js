import { Card, Stack, Typography } from "@mui/material";
import { Box } from "@mui/system";
import React, { useState } from "react";
import { useParams } from "react-router-dom";
import useAxiosGet from "../../../../hooks/useAxiosGet";
import { _getWpPrefix } from "../../../../utils/helpers";
import Button from "../../../UI/Button/Button";
import AppLoader from "../../../UI/loading/AppLoader/AppLoader";
import SidedrawerModal from "../../../UI/SidedrawerModal/SidedrawerModal";
import Heading from "../../../UI/typography/Heading";
import WorkPackageForm, { WP_FORM_MODES } from "./WorkPackageForm";
import WorkPackageEffortForm, {
  WP_EFFORT_FORM_MODES,
} from "./WorkPackageEffortForm";
import styles from "./WorkPackagesDetails.module.css";
import WorkPackages from "./WorkPackages";
import { useUser } from "../../../../contexts/userContext";
import WorkPackageEmployees from "./WorkPackageEmployees";

const dayEstimateLabels = {
  p1PlannedDays: "P1 Days",
  p2PlannedDays: "P2 Days",
  p3PlannedDays: "P3 Days",
  p4PlannedDays: "P4 Days",
  p5PlannedDays: "P5 Days",
  jsPlannedDays: "JS Days",
  dsPlannedDays: "DS Days",
  ssPlannedDays: "SS Days",
};

const WP_FORM_MODAL_DEFAULT_STATE = {
  open: false,
  mode: null,
  wp: null,
};

const WP_EFFORT_FORM_MODAL_DEFAULT_STATE = {
  open: false,
  mode: null,
  wpEstimate: null,
};

const WorkPackageDetails = () => {
  const { projectID, workpackageID } = useParams();
  const { user } = useUser();
  const {
    response: workPackage,
    isLoading,
    fetchData: refetchWorkPackage,
  } = useAxiosGet(`/work-package/?proj-id=${projectID}&wp-id=${workpackageID}`);

  const { response: project } = useAxiosGet(`/project/${projectID}`);

  const {
    response: wpEffortEstimate,
    fetchData: refetchWpEffortEstimate,
    error: wpEffortEstimateError,
  } = useAxiosGet(
    `/project-workpackage-estimate/?workpackageID=${workpackageID}&projectID=${projectID}`
  );

  const {
    response: wpCostEstimate,
    fetchData: refetchWpCostEstimate,
    error: wpCostEstimateError,
  } = useAxiosGet(
    `/workpackage-estimate-costs/?workpackageID=${workpackageID}&projectID=${projectID}`
  );

  const {
    response: wpCompletionEstimate,
    fetchData: refetchWpCompletionEstimate,
    error: wpCompletionEstimateError,
  } = useAxiosGet(
    `/workpackage-estimate-to-completion/?workpackageID=${workpackageID}&projectID=${projectID}`
  );

  const [wpFormModal, setWpFormModal] = useState(WP_FORM_MODAL_DEFAULT_STATE); // Edit WP State
  const [wpEffortFormModal, setWpEffortFormModal] = useState(
    WP_EFFORT_FORM_MODAL_DEFAULT_STATE
  ); // Enter Effort WP State

  const { response: employees } = useAxiosGet("/employee");

  const {
    workPackageTitle,
    chargable,
    completed,
    responsibleEngineerID,
    unallocatedBudget,
    workpackageEndDate,
    workpackageStartDate,
  } = workPackage;

  const { response: responsibleEngineer } = useAxiosGet(
    `/employee/${responsibleEngineerID}`,
    !!responsibleEngineerID
  );

  const handleEditWp = () => {
    setWpFormModal({ open: true, mode: WP_FORM_MODES.EDIT, wp: workPackage });
  };

  // Project Manager edit estimates
  const handleEditPmEstimates = () => {
    setWpEffortFormModal({
      open: true,
      mode: WP_EFFORT_FORM_MODES.PM_EDIT,
      wpEstimate: wpEffortEstimate,
    });
  };

  // Responsible Engineer create estimates
  const handleCreateReEstimates = () => {
    setWpEffortFormModal({
      open: true,
      mode: WP_EFFORT_FORM_MODES.RES_ENG_CREATE,
      wpEstimate: wpCostEstimate,
    });
  };

  // Responsible Engineer edit estimates
  const handleEditReEstimates = () => {
    setWpEffortFormModal({
      open: true,
      mode: WP_EFFORT_FORM_MODES.RES_ENG_EDIT,
      wpEstimate: wpCostEstimate,
    });
  };

  // Responsible Engineer create estimates (to completion)
  const handleCreateReEstimatesToCompletion = () => {
    setWpEffortFormModal({
      open: true,
      mode: WP_EFFORT_FORM_MODES.RES_ENG_CREATE_TO_COMPL,
      wpEstimate: wpCompletionEstimate,
    });
  };

  // Responsible Engineer edit estimates (to completion)
  const handleEditReEstimatesToCompletion = () => {
    setWpEffortFormModal({
      open: true,
      mode: WP_EFFORT_FORM_MODES.RES_ENG_EDIT_TO_COMPL,
      wpEstimate: wpCompletionEstimate,
    });
  };

  const closeWpForm = () => {
    setWpFormModal(WP_FORM_MODAL_DEFAULT_STATE);
  };

  const closeEnterEffortWpForm = () => {
    setWpEffortFormModal(WP_EFFORT_FORM_MODAL_DEFAULT_STATE);
  };

  const _formatEstimateLabel = (key) => {
    return dayEstimateLabels[key];
  };

  const canEditWP = () =>
    !completed && (user.employeeID === project.employeeID || user.isAdmin);

  if (isLoading) return <AppLoader centerInParent />;

  return (
    <Box>
      <Typography variant="caption2" sx={{ fontSize: "0.75rem" }}>
        WORK PACKAGE
      </Typography>
      <Box
        display="flex"
        justifyContent="space-between"
        alignItems="flex-start"
        mb={2}
      >
        <Heading>{workPackageTitle}</Heading>

        <div id={styles.buttonContainer}>
          {canEditWP() && (
            <Button icon="edit" onClick={handleEditWp}>
              Edit Work Package
            </Button>
          )}
        </div>
      </Box>

      <Card sx={{ p: 3, mb: 3 }} variant="outlined">
        <Stack direction="column" spacing={2}>
          <Stack direction="row" spacing={5} justifyContent="space-between">
            <Box>
              <Typography variant="caption">ID</Typography>
              <Typography variant="h6">{workpackageID}</Typography>
            </Box>
            <Box>
              <Typography variant="caption">Responsible Engineer</Typography>
              <Typography variant="h6">{`${responsibleEngineer.firstName} ${responsibleEngineer.lastName}`}</Typography>
            </Box>
            <Box>
              <Typography variant="caption">Chargable</Typography>
              <Typography variant="h6">{chargable ? "Yes" : "No"}</Typography>
            </Box>
            <Box>
              <Typography variant="caption">Completed</Typography>
              <Typography variant="h6">{completed ? "Yes" : "No"}</Typography>
            </Box>
            <Box>
              <Typography variant="caption">Unallocated Budget</Typography>
              <Typography variant="h6">${unallocatedBudget}</Typography>
            </Box>
            <Box>
              <Typography variant="caption">Start Date</Typography>
              <Typography variant="h6">{workpackageStartDate}</Typography>
            </Box>
            <Box>
              <Typography variant="caption">End Date</Typography>
              <Typography variant="h6">{workpackageEndDate}</Typography>
            </Box>
          </Stack>
        </Stack>
      </Card>

      {_getWpPrefix(workpackageID)?.length < 5 && (
        <WorkPackages
          workpackageID={workpackageID}
          projectID={projectID}
          canEdit={canEditWP}
          refetchParent={refetchWorkPackage}
        />
      )}

      <WorkPackageEmployees
        projectID={projectID}
        workPackageID={workpackageID}
      />

      {/* Project Manager estimates */}
      <Card variant="outlined" sx={{ p: 3, mt: 3 }}>
        <Box display="flex" justifyContent="space-between" mb={3}>
          <Typography variant="h5">Project Manager Effort Estimates</Typography>
          {project.employeeID === user.employeeID && (
            <Button icon="edit" onClick={() => handleEditPmEstimates()}>
              Edit Estimates
            </Button>
          )}
        </Box>
        {!wpEffortEstimateError && (
          <Stack direction="column" spacing={2}>
            <Stack direction="row" spacing={5} justifyContent="space-between">
              {Object.entries(wpEffortEstimate)
                .filter(
                  ([key]) => key !== "projectID" && key !== "workPackageID"
                )
                .map(([key, val]) => (
                  <Box key={key}>
                    <Typography variant="caption">
                      {_formatEstimateLabel(key)}
                    </Typography>
                    <Typography variant="h6">{val}</Typography>
                  </Box>
                ))}
            </Stack>
          </Stack>
        )}
      </Card>

      {/* Responsible Engineer estimates */}
      <Card variant="outlined" sx={{ p: 3, mt: 3 }}>
        <Box display="flex" justifyContent="space-between">
          <Typography variant="h5">
            Responsible Engineer Effort Estimates
          </Typography>
          {user.employeeID === responsibleEngineerID && (
            <Button
              icon={wpCostEstimateError ? "add" : "edit"}
              onClick={
                wpCostEstimateError
                  ? handleCreateReEstimates
                  : handleEditReEstimates
              }
            >
              {wpCostEstimateError ? "Create Estimates" : "Edit Estimates"}
            </Button>
          )}
        </Box>
        {!wpCostEstimateError && (
          <>
            <Stack direction="column" spacing={2}>
              <Stack direction="row" spacing={5} justifyContent="space-between">
                {Object.entries(wpCostEstimate)
                  .filter(
                    ([key]) => key !== "projectID" && key !== "workPackageID"
                  )
                  .map(([key, val]) => (
                    <Box key={key}>
                      <Typography variant="caption">
                        {_formatEstimateLabel(key)}
                      </Typography>
                      <Typography variant="h6">{val}</Typography>
                    </Box>
                  ))}
              </Stack>
            </Stack>
          </>
        )}
      </Card>

      {/* Responsible Engineer estimates to completion */}
      <Card variant="outlined" sx={{ p: 3, mt: 3 }}>
        <Box
          display="flex"
          justifyContent="space-between"
          alignItems="flex-start"
        >
          <Typography variant="h5">
            Responsible Engineer Estimates to Completion
          </Typography>
          {user.employeeID === responsibleEngineerID && (
            <Button
              icon={wpCompletionEstimateError ? "add" : "edit"}
              onClick={
                wpCompletionEstimateError
                  ? handleCreateReEstimatesToCompletion
                  : handleEditReEstimatesToCompletion
              }
            >
              {wpCompletionEstimateError
                ? "Create Estimates"
                : "Edit Estimates"}
            </Button>
          )}
        </Box>
        {!wpCompletionEstimateError && (
          <>
            <Typography variant="body2" sx={{ my: 2 }}>
              Estimated {wpCompletionEstimate.percentageComplete}% Complete
            </Typography>

            <Stack direction="column" spacing={2}>
              <Stack direction="row" spacing={5} justifyContent="space-between">
                {Object.entries(wpCompletionEstimate)
                  .filter(
                    ([key]) =>
                      key !== "projectID" &&
                      key !== "workPackageID" &&
                      key !== "percentageComplete"
                  )
                  .map(([key, val]) => (
                    <Box key={key}>
                      <Typography variant="caption">
                        {_formatEstimateLabel(key)}
                      </Typography>
                      <Typography variant="h6">{val}</Typography>
                    </Box>
                  ))}
              </Stack>
            </Stack>
          </>
        )}
      </Card>

      {/* Edit Work Package Form */}
      <SidedrawerModal show={wpFormModal.open} closeModal={closeWpForm}>
        <WorkPackageForm
          closeModal={closeWpForm}
          mode={wpFormModal.mode}
          wp={wpFormModal.wp}
          empList={employees}
          projectID={projectID}
          refetchWorkPackage={refetchWorkPackage}
        />
      </SidedrawerModal>

      {/* Enter Effort Required Form */}
      <SidedrawerModal
        show={wpEffortFormModal.open}
        closeModal={closeEnterEffortWpForm}
      >
        <WorkPackageEffortForm
          closeModal={closeEnterEffortWpForm}
          mode={wpEffortFormModal.mode}
          wpEstimate={wpEffortFormModal.wpEstimate}
          workPackageID={workpackageID}
          projectID={projectID}
          refetchWpEffortEstimate={refetchWpEffortEstimate}
          refetchWpCostEstimate={refetchWpCostEstimate}
          refetchWpCompletionEstimate={refetchWpCompletionEstimate}
        />
      </SidedrawerModal>
    </Box>
  );
};

export default WorkPackageDetails;
