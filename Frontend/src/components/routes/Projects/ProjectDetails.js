import React, { useState } from "react";
import { Box, Card, Stack } from "@mui/material";
import { useNavigate, useParams } from "react-router-dom";
import Typography from "@mui/material/Typography";
import useAxiosGet from "../../../hooks/useAxiosGet";
import SidedrawerModal from "../../UI/SidedrawerModal/SidedrawerModal";
import ProjectForm from "./ProjectForm";
import Button from "../../UI/Button/Button";
import { PROJECT_FORM_MODES as modes } from "./ProjectForm";
import AppLoader from "../../UI/loading/AppLoader/AppLoader";
import WorkPackages from "./WorkPackages/WorkPackages";
import ProjectEmployees from "./ProjectEmployees/ProjectEmployees";
import { useUser } from "../../../contexts/userContext";
import rp from "../../routing/routePaths";
import classes from "./ProjectDetails.module.css";

const DEFAULT_MODAL_STATE = { show: false, mode: null, project: null };

const ProjectDetails = () => {
  const { user } = useUser();
  const { projectID } = useParams();
  const {
    response: project,
    fetchData: refetchProject,
    isLoading: projectsLoading,
  } = useAxiosGet(`/project/${projectID}`);
  const { response: employees, isLoading: employeesLoading } =
    useAxiosGet("/employee");
  const [modalState, setModalState] = useState(DEFAULT_MODAL_STATE);
  const nav = useNavigate();

  const closeModal = () => setModalState(DEFAULT_MODAL_STATE);

  const editHandler = () => {
    setModalState({ show: true, mode: modes.EDIT, project });
  };

  const projectManager = employees.find(
    (e) => e.employeeID === project?.employeeID
  );
  const projectAssistant = employees.find(
    (e) => e.employeeID === project?.assistantID
  );

  const _canEditProject = () => {
    return (
      project?.activeProject &&
      (user.isAdmin || project.employeeID === user.employeeID)
    );
  };

  const _canGenerateReport = () => {
    return (
      user.isAdmin ||
      project.employeeID === user.employeeID ||
      project.assistantID === user.employeeID
    );
  };

  const reportHandler = () => {
    nav(rp.projectReport.path.replace(":projectID", project.projectID));
  };

  if (projectsLoading || employeesLoading) return <AppLoader centerInParent />;
  return (
    <Box>
      <Typography variant="caption2" sx={{ fontSize: "0.75rem" }}>
        PROJECT
      </Typography>

      <Box display="flex" justifyContent="space-between" mb={2}>
        <Typography variant="h4">{project.projectName}</Typography>

        <Box display="flex" gap={1}>
          {_canEditProject() && (
            <Button size="small" onClick={editHandler} icon="edit">
              Edit Project
            </Button>
          )}
          {_canGenerateReport() && (
            <Button size="small" onClick={reportHandler} icon="report">
              Generate Report
            </Button>
          )}
        </Box>
      </Box>

      <Card sx={{ p: 3, mb: 3 }} variant="outlined">
        <Stack direction="column" spacing={2}>
          <Stack direction="row" spacing={5} justifyContent="space-between">
            <Box>
              <Typography variant="caption">ID</Typography>
              <Typography variant="h6">{project.projectID}</Typography>
            </Box>
            <Box>
              <Typography variant="caption">Manager</Typography>
              <Typography variant="h6">{`${projectManager.firstName}  ${projectManager.lastName}`}</Typography>
            </Box>
            <Box>
              <Typography variant="caption">Assistant</Typography>
              <Typography variant="h6">{`${projectAssistant.firstName}  ${projectAssistant.lastName}`}</Typography>
            </Box>
            <Box>
              <Typography variant="caption">Start Date</Typography>
              <Typography variant="h6">{project.projectStart}</Typography>
            </Box>
            <Box>
              <Typography variant="caption">Due Date</Typography>
              <Typography variant="h6">{project.projectEnd}</Typography>
            </Box>
            <Box>
              <Typography variant="caption">Markup</Typography>
              <Typography variant="h6">{project.projectMarkup}</Typography>
            </Box>
            <Box>
              <Typography variant="caption">Budget</Typography>
              <Typography variant="h6">${project.projectBudget}</Typography>
            </Box>
            <Box>
              <Typography variant="caption">Unallocated Budget</Typography>
              <Typography variant="h6">${project.unallocatedBudget}</Typography>
            </Box>
            <Box>
              <Typography variant="caption">Status</Typography>
              <Typography
                variant="h6"
                className={classes[project.activeProject ? "active" : "closed"]}
              >
                {project?.activeProject ? "ACTIVE" : "CLOSED"}
              </Typography>
            </Box>
          </Stack>
        </Stack>
      </Card>

      {/* Create & edit project modal */}
      <SidedrawerModal show={modalState.show} closeModal={closeModal}>
        <ProjectForm
          closeModal={closeModal}
          mode={modalState.mode}
          project={modalState.project}
          refetchProject={refetchProject}
          empList={employees}
        />
      </SidedrawerModal>

      <WorkPackages
        projectID={projectID}
        canEdit={_canEditProject}
        refetchParent={refetchProject}
      />
      <ProjectEmployees
        projectID={projectID}
        isProjectAcitve={project.activeProject}
      />
    </Box>
  );
};

export default ProjectDetails;
