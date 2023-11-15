import React, { useState } from "react";
import { Box, Card, Typography } from "@mui/material";
import Table from "../../../UI/Table/Table";
import useAxiosGet from "../../../../hooks/useAxiosGet";
import { WP_HEADERS, WP_UNIQUE_ID } from "./WorkPackageHS";
import { useNavigate } from "react-router-dom";
import Button from "../../../UI/Button/Button";
import classes from "../Projects.module.css";
import AddIcon from "@mui/icons-material/Add";
import SidedrawerModal from "../../../UI/SidedrawerModal/SidedrawerModal";
import WorkPackageForm, { WP_FORM_MODES } from "./WorkPackageForm";
import Modal from "@mui/material/Modal";
import errorImage from "../../../../assets/images/error.png";
import {
  completeWorkPackage,
  deleteWorkPackage,
} from "../../../../services/api/workPackageAxios";

const CLOSE_WP_MODAL_DEFAULT_STATE = {
  open: false,
  wp: null,
  mode: null,
};

const WP_FORM_MODAL_DEFAULT_STATE = {
  open: false,
  mode: null,
  wp: null,
};

const WorkPackages = ({ projectID, workpackageID, canEdit, refetchParent }) => {
  const { response: workPackageList, fetchData: refetchWorkPackages } =
    useAxiosGet(
      workpackageID
        ? `/work-package/${workpackageID}/?projectId=${projectID}`
        : `/work-package/00000/?projectId=${projectID}`
    );
  const { response: employees } = useAxiosGet("/employee");

  const [wpFormModal, setWpFormModal] = useState(WP_FORM_MODAL_DEFAULT_STATE);

  const [closeWpModal, setCloseWpModal] = useState(
    CLOSE_WP_MODAL_DEFAULT_STATE
  );

  const navigate = useNavigate();

  const onView = (wp) => {
    navigate(`/projects/${projectID}/work-packages/${wp.workpackageID}`);
  };

  const onCloseWp = (wp) => {
    setCloseWpModal({ open: true, wp, mode: "close" });
  };

  const closeCloseWp = () => {
    setCloseWpModal(CLOSE_WP_MODAL_DEFAULT_STATE);
  };

  const handleCloseWp = async () => {
    await completeWorkPackage(closeWpModal.wp);
    refetchWorkPackages();
    closeCloseWp();
  };

  const handleDeleteWp = async () => {
    await deleteWorkPackage(closeWpModal.wp);
    refetchWorkPackages();
    closeCloseWp();
  };

  const handleCreateWP = () => {
    setWpFormModal({ open: true, mode: WP_FORM_MODES.CREATE, wp: null });
  };

  const closeWpForm = () => {
    setWpFormModal(WP_FORM_MODAL_DEFAULT_STATE);
  };

  const onDeleteWp = (wp) => {
    setCloseWpModal({ open: true, wp, mode: "delete" });
  };

  return (
    <Card variant="outlined" sx={{ p: 3 }}>
      <Box display="flex" justifyContent="space-between">
        <Typography variant="h5" mb={2}>
          Work Packages
        </Typography>
        <Box display="flex" justifyContent="flex-end">
          {canEdit() && (
            <Button onClick={handleCreateWP} sx={{ mb: 2 }}>
              <AddIcon />
              <Typography variant="body2" ml={1}>
                Create Work Package
              </Typography>
            </Button>
          )}
        </Box>
      </Box>

      <Table
        data={workPackageList}
        headers={WP_HEADERS(onView, onCloseWp, onDeleteWp, canEdit)}
        uniqueIdSrc={WP_UNIQUE_ID}
      />

      <Modal open={closeWpModal.open} onClose={closeCloseWp}>
        <Box
          sx={{
            position: "absolute",
            top: "35%",
            left: "50%",
            transform: "translate(-50%, -50%)",
            width: 400,
            bgcolor: "background.paper",
            borderRadius: "8px",
            boxShadow: 24,
            p: 3,
          }}
        >
          <div id={classes.modalContainer}>
            <div id={classes.imageContainer}>
              <img src={errorImage} id={classes.image} alt="A Red X"></img>
            </div>
            <div id={classes.deleteHeaderContainer}>
              <Typography
                variant="h4"
                component="h1"
                sx={{ fontWeight: "500", fontSize: "24pt" }}
              >
                {closeWpModal.mode === "close" ? "Close" : "Delete"} work
                package with ID: {closeWpModal?.wp?.workpackageID}?
              </Typography>
            </div>
            <div id={classes.deleteDescriptionContainer}>
              <p>
                {closeWpModal.mode === "close"
                  ? "Closing this work package marks it as complete and charges it. All activities performed to this work package will be disabled. This process cannot be undone."
                  : "Deleting this work package erases it permanently. This process cannot be undone."}
              </p>
            </div>
            <div id={classes.buttonContainer}>
              <Button variant="outlined" color="inherit" onClick={closeCloseWp}>
                Cancel
              </Button>
              <Button
                color="error"
                onClick={
                  closeWpModal.mode === "close" ? handleCloseWp : handleDeleteWp
                }
              >
                {closeWpModal.mode === "close" ? "Close" : "Delete"}
              </Button>
            </div>
          </div>
        </Box>
      </Modal>

      {/* Create & edit modal */}
      <SidedrawerModal show={wpFormModal.open} closeModal={closeWpForm}>
        <WorkPackageForm
          closeModal={closeWpForm}
          mode={wpFormModal.mode}
          wp={wpFormModal.wp}
          refetchWorkPackages={refetchWorkPackages}
          refetchParent={refetchParent}
          empList={employees}
          projectID={projectID}
          parentWpID={workpackageID}
        />
      </SidedrawerModal>
    </Card>
  );
};

export default WorkPackages;
