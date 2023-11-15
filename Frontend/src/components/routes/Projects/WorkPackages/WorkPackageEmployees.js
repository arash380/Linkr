import React, { useCallback, useEffect, useReducer, useState } from "react";
import Table from "../../../UI/Table/Table";
import { EMPLOYEE_HEADERS, EMPLOYEE_UNIQUE_ID } from "../createProjectHS";
import classes from "../CreateProjectForm.module.css";
import useAxiosGet from "../../../../hooks/useAxiosGet";
import { Box, Card, Typography } from "@mui/material";
import Button from "../../../UI/Button/Button";
import SidedrawerModal from "../../../UI/SidedrawerModal/SidedrawerModal";
import { useUser } from "../../../../contexts/userContext";
import { WP_EMPLOYEES_HEADERS } from "./WorkPackageEmployeesHS";
import { EMPLOYEE_ADD_UNIQUE_ID } from "../AddEmployeeHS";
import { updateWpEmployees } from "../../../../services/api/workPackageEmployeesAxios";

const DEFAULT_MODAL_STATE = { show: false };

const wpEmployeesReducer = (state, action) => {
  switch (action.type) {
    case "initialize": {
      return [...action.payload.map((e) => e.employeeID)];
    }
    case "toggleEmployee": {
      if (
        !state.find((employeeID) => employeeID === action.payload.employeeID)
      ) {
        return [...state, action.payload.employeeID];
      } else {
        return [
          ...state.filter(
            (employeeID) => employeeID !== action.payload.employeeID
          ),
        ];
      }
    }
    default:
      return state;
  }
};

const WorkPackageEmployees = ({ projectID, workPackageID }) => {
  const { user } = useUser();

  const {
    response: assignedEmployees,
    fetchData: refetchAssignedEmployees,
    error: assignedEmployeesError,
  } = useAxiosGet(
    `/workpackage-employee/?workpackageID=${workPackageID}&projectID=${projectID}`
  );

  const { response: assignableEmployees, error: assignableEmployeesError } =
    useAxiosGet(`/project-Employee/?projectID=${projectID}`, !!user);

  const { response: allEmployees } = useAxiosGet(`/employee`);

  const { response: project } = useAxiosGet(`/project/${projectID}`);

  const [modalState, setModalState] = useState(DEFAULT_MODAL_STATE);
  const [wpEmps, dispatchWpEmps] = useReducer(wpEmployeesReducer, []);

  const closeModal = () => {
    setModalState(DEFAULT_MODAL_STATE);
    dispatchWpEmps({
      type: "initialize",
      payload: assignedEmployees,
    });
  };

  const _empIsAssigned = useCallback(
    (emp) => {
      return wpEmps.find((employeeID) => employeeID === emp.employeeID)
        ? true
        : false;
    },
    [wpEmps]
  );

  useEffect(() => {
    dispatchWpEmps({
      type: "initialize",
      payload: assignedEmployees,
    });
  }, [assignedEmployees]);

  const handleToggleAssign = (emp) => {
    dispatchWpEmps({ type: "toggleEmployee", payload: emp });
  };

  const handleSaveWpEmps = async () => {
    await updateWpEmployees(projectID, workPackageID, wpEmps);
    closeModal();
    refetchAssignedEmployees();
  };

  return (
    <Card variant="outlined" sx={{ p: 3, mt: 3 }}>
      <Box display="flex" justifyContent="space-between">
        <Typography variant="h5" mb={2}>
          Assigned Employees
        </Typography>
        {!assignableEmployeesError &&
          (user?.isAdmin ||
            user?.employeeID === project?.employeeID ||
            user?.employeeID === project?.assistantID) && (
            <Box display="flex" justifyContent="flex-end">
              <Button
                icon="edit"
                onClick={() => setModalState({ show: true })}
                sx={{ mb: 2 }}
              >
                <Typography variant="body2" ml={1}>
                  Manage Employees
                </Typography>
              </Button>
            </Box>
          )}
      </Box>

      <SidedrawerModal show={modalState.show} closeModal={closeModal}>
        <Typography variant="body1" sx={{ my: 2 }}>
          <strong>Note: </strong> The employees listed below are the employees
          belonging to the project.
        </Typography>
        <Table
          data={assignableEmployees}
          headers={WP_EMPLOYEES_HEADERS(
            handleToggleAssign,
            _empIsAssigned,
            allEmployees
          )}
          uniqueIdSrc={EMPLOYEE_ADD_UNIQUE_ID}
        />
        <Button className={classes.doneButton} onClick={handleSaveWpEmps}>
          Save
        </Button>
      </SidedrawerModal>

      <Table
        data={assignedEmployeesError ? [] : assignedEmployees}
        headers={EMPLOYEE_HEADERS(allEmployees)}
        uniqueIdSrc={EMPLOYEE_UNIQUE_ID}
        className={classes.empTable}
      />
    </Card>
  );
};

export default WorkPackageEmployees;
