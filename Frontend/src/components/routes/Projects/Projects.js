import React, { useState } from "react";
import { Box } from "@mui/material";
import { useNavigate } from "react-router-dom";
import Table from "../../UI/Table/Table";
import { PROJECT_HEADERS, PROJECT_UNIQUE_ID } from "./ProjectHS";
import Heading from "../../UI/typography/Heading";
import rp from "../../routing/routePaths";
import Typography from "@mui/material/Typography";
import Modal from "@mui/material/Modal";
import errorImage from "../../../assets/images/error.png";
import classes from "./Projects.module.css";
import useAxiosGet from "../../../hooks/useAxiosGet";
import Button from "../../UI/Button/Button";
import { toggleProjectActive } from "../../../services/api/projectAxios";
import SidedrawerModal from "../../UI/SidedrawerModal/SidedrawerModal";
import ProjectForm, { PROJECT_FORM_MODES } from "./ProjectForm";
import { useUser } from "../../../contexts/userContext";
import AppLoader from "../../UI/loading/AppLoader/AppLoader";

const modalStyle = {
  position: "absolute",
  top: "35%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: 400,
  bgcolor: "background.paper",
  borderRadius: "8px",
  boxShadow: 24,
  p: 3,
};

const CLOSE_PROJECT_MODAL_DEFAULT_STATE = {
  open: false,
  project: null,
};

const PROJECT_FORM_MODAL_DEFAULT_STATE = {
  open: false,
  mode: null,
  project: null,
};

const Projects = () => {
  const { user } = useUser();
  const {
    response: projectList,
    fetchData: refetchProjects,
    isLoading,
  } = useAxiosGet(`/project`);
  const { response: assignedProjects } = useAxiosGet(
    `/project-Employee/?employeeID=${user.employeeID}`
  );
  const { response: employees } = useAxiosGet("/employee");
  const { isLoading: childEmpsLoading, error: childEmpsErr } = useAxiosGet(
    `/employee/?supervisor-id=${user.employeeID}`
  );

  const notSupervisor = childEmpsLoading ? true : childEmpsErr;

  const navigate = useNavigate();

  const [projectFormModal, setProjectFormModal] = useState(
    PROJECT_FORM_MODAL_DEFAULT_STATE
  );

  const [closeProjectModal, setCloseProjectModal] = useState(
    CLOSE_PROJECT_MODAL_DEFAULT_STATE
  );

  const handleClose = () => {
    setCloseProjectModal(CLOSE_PROJECT_MODAL_DEFAULT_STATE);
  };

  const handleProjectFormClose = () => {
    setProjectFormModal(PROJECT_FORM_MODAL_DEFAULT_STATE);
  };

  const onViewProject = (projectID) => {
    navigate(rp.projectDetails.path.replace(":projectID", projectID));
  };

  const onCloseProject = async (project) => {
    setCloseProjectModal({ project, open: true });
  };

  const handleCreateProject = () => {
    setProjectFormModal({
      open: true,
      mode: PROJECT_FORM_MODES.CREATE,
      project: null,
    });
  };

  const closeProject = async () => {
    await toggleProjectActive(closeProjectModal.project);
    refetchProjects();
    handleClose();
  };

  // Admins will see every project
  // Others will see a project if they're part of it
  // Or if they are its manager or assistant
  const _getProjects = () => {
    const projects = projectList.sort(
      (p1, p2) => p2.activeProject - p1.activeProject
    );

    if (user.isAdmin || !notSupervisor) return projects;

    return projects.filter((p) => {
      if (p.employeeID === user.employeeID || p.assistantID === user.employeeID)
        return true;

      return assignedProjects.some((assignedProject) => {
        return assignedProject.projectID === p.projectID;
      });
    });
  };

  const _canCreateProject = () => user.isAdmin || !notSupervisor;
  const _canEditProject = (p) =>
    user.isAdmin ||
    p.employeeID === user.employeeID ||
    p.assistantID === user.employeeID;

  return (
    <Box>
      <Heading>{rp.projects.name}</Heading>
      {_canCreateProject() && (
        <Box display="flex" justifyContent="flex-end">
          <Button
            className={classes.btn}
            onClick={handleCreateProject}
            icon="add"
            sx={{ mb: 2 }}
          >
            <Typography variant="body2">Create Project</Typography>
          </Button>
        </Box>
      )}

      {isLoading ? (
        <AppLoader centerInParent />
      ) : (
        <Table
          data={_getProjects()}
          headers={PROJECT_HEADERS(
            onViewProject,
            onCloseProject,
            _canEditProject
          )}
          uniqueIdSrc={PROJECT_UNIQUE_ID}
        />
      )}

      <Modal open={closeProjectModal.open} onClose={handleClose}>
        <Box sx={modalStyle}>
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
                Close This Project?
              </Typography>
            </div>
            <div id={classes.deleteDescriptionContainer}>
              <p>
                Closing this project marks it as complete. All activities
                performed to this project will be disabled. This process cannot
                be undone.
              </p>
            </div>
            <div id={classes.buttonContainer}>
              <Button variant="outlined" color="inherit" onClick={handleClose}>
                Cancel
              </Button>
              <Button color="error" onClick={closeProject}>
                Close
              </Button>
            </div>
          </div>
        </Box>
      </Modal>

      {/* Create & edit modal */}
      <SidedrawerModal
        show={projectFormModal.open}
        closeModal={handleProjectFormClose}
      >
        <ProjectForm
          closeModal={handleProjectFormClose}
          mode={projectFormModal.mode}
          project={projectFormModal.project}
          refetchProjects={refetchProjects}
          empList={employees}
        />
      </SidedrawerModal>
    </Box>
  );
};

export default Projects;
