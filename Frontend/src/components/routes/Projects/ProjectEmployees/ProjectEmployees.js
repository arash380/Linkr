import React, { useCallback, useEffect, useReducer, useState } from "react";
import Table from "../../../UI/Table/Table";
import { EMPLOYEE_HEADERS, EMPLOYEE_UNIQUE_ID } from "../createProjectHS";
import classes from "../CreateProjectForm.module.css";
import useAxiosGet from "../../../../hooks/useAxiosGet";
import { Box, Card, Typography } from "@mui/material";
import Button from "../../../UI/Button/Button";
import SidedrawerModal from "../../../UI/SidedrawerModal/SidedrawerModal";
import { EMPLOYEE_ADD_HEADERS, EMPLOYEE_ADD_UNIQUE_ID } from "../AddEmployeeHS";
import { updateProjectEmployees } from "../../../../services/api/projectEmployeeAxios";
import { useUser } from "../../../../contexts/userContext";
import { toast } from "react-toastify";

const DEFAULT_MODAL_STATE = { show: false };

const projectEmployeesReducer = (state, action) => {
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

const ProjectEmployees = ({ projectID, isProjectAcitve }) => {
  const { response: allWpEmps } = useAxiosGet(
    `/workpackage-employee/assigned-employees?projectID=${projectID}`
  );

  const {
    response: assignedEmployees,
    fetchData: refetchAssignedEmployees,
    error: assignedEmployeesError,
  } = useAxiosGet(`/project-Employee/?projectID=${projectID}`);
  const { user } = useUser();
  const { response: assignableEmployees, error: assignableEmployeesError } =
    useAxiosGet(
      user.isAdmin
        ? `/employee`
        : `/employee/?supervisor-id=${user.employeeID}`,
      !!user
    );
  const { response: allEmployees } = useAxiosGet(`/employee`);
  const [modalState, setModalState] = useState(DEFAULT_MODAL_STATE);
  const [projectEmps, dispatchProjectEmps] = useReducer(
    projectEmployeesReducer,
    []
  );

  const closeModal = () => {
    setModalState(DEFAULT_MODAL_STATE);
    dispatchProjectEmps({
      type: "initialize",
      payload: assignedEmployees,
    });
  };

  const _empIsAssigned = useCallback(
    (emp) => {
      return projectEmps.find((employeeID) => employeeID === emp.employeeID)
        ? true
        : false;
    },
    [projectEmps]
  );

  useEffect(() => {
    dispatchProjectEmps({
      type: "initialize",
      payload: assignedEmployees,
    });
  }, [assignedEmployees]);

  const handleToggleAssign = (emp) => {
    dispatchProjectEmps({ type: "toggleEmployee", payload: emp });
  };

  const handleSaveProjectEmps = async () => {
    const unassignedEmps = assignableEmployees.filter(
      (e) => !projectEmps.some((empID) => empID === e.employeeID)
    );
    const unremovableEmps = unassignedEmps
      .filter((e) => employeeHasWp(e.employeeID))
      .map((e) => e.employeeID);

    if (unremovableEmps.length > 0) {
      toast.error(
        `Employees with the following IDs cannot be removed from the project because they are assigned to work packages: ${unremovableEmps.join(
          ", "
        )}`
      );
    } else {
      await updateProjectEmployees(projectID, projectEmps);
      closeModal();
      refetchAssignedEmployees();
    }
  };

  const employeeHasWp = (employeeID) => {
    return allWpEmps.some((wpe) => wpe.employeeID === employeeID);
  };

  return (
    <Card variant="outlined" sx={{ p: 3, mt: 3 }}>
      <Box display="flex" justifyContent="space-between">
        <Typography variant="h5" mb={2}>
          Assigned Employees
        </Typography>
        {!assignableEmployeesError && isProjectAcitve && (
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
          you supervise unless you're the admin in which case you'll see every
          employee. There may be other employees assigned to the project which
          would be listed in the Assigned Employees table.
        </Typography>
        <Table
          data={assignableEmployees}
          headers={EMPLOYEE_ADD_HEADERS(handleToggleAssign, _empIsAssigned)}
          uniqueIdSrc={EMPLOYEE_ADD_UNIQUE_ID}
        />
        <Button className={classes.doneButton} onClick={handleSaveProjectEmps}>
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

export default ProjectEmployees;
