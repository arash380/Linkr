import React, { useState } from "react";
import { Box, Typography } from "@mui/material";
import Heading from "../../UI/typography/Heading";
import useAxiosGet from "../../../hooks/useAxiosGet";
import Table from "../../UI/Table/Table";
import { EMPLOYEE_HEADERS, EMPLOYEE_UNIQUE_ID } from "./employeeHS";
import classes from "./Employees.module.css";
import SidedrawerModal from "../../UI/SidedrawerModal/SidedrawerModal";
import EmployeeForm, { EMP_FORM_MODES as modes } from "./EmployeeForm";
import { disableOrEnableEmployee } from "../../../services/api/employeeAxios";
import AddIcon from "@mui/icons-material/Add";
import Button from "../../UI/Button/Button";
import { useUser } from "../../../contexts/userContext";

export const PAY_RATES = ["JS", "DS", "P1", "P2", "P3", "P4", "P5", "P6"];

const DEFAULT_MODAL_STATE = { show: false, mode: null, emp: null };

const Employees = () => {
  const { response: employees, fetchData } = useAxiosGet("/employee");
  const [modalState, setModalState] = useState(DEFAULT_MODAL_STATE);
  const { user } = useUser();

  const onCreateEmp = () => {
    setModalState({ show: true, mode: modes.CREATE, emp: null });
  };

  const onEditEmp = (emp) => {
    setModalState({ show: true, mode: modes.EDIT, emp });
  };

  const onDisableEmp = async (emp) => {
    await disableOrEnableEmployee(emp);
    fetchData();
  };

  const closeModal = () => setModalState(DEFAULT_MODAL_STATE);

  const _getEmps = () =>
    employees?.filter((emp) => emp.employeeID !== user.employeeID);

  return (
    <Box>
      <Heading>Employees</Heading>
      <Box display="flex" justifyContent="flex-end">
        <Button className={classes.btn} onClick={onCreateEmp} sx={{ mb: 2 }}>
          <AddIcon />
          <Typography variant="body2" ml={1}>
            Create Employee
          </Typography>
        </Button>
      </Box>
      <Table
        data={_getEmps()}
        headers={EMPLOYEE_HEADERS(onEditEmp, onDisableEmp, employees)}
        uniqueIdSrc={EMPLOYEE_UNIQUE_ID}
      />

      {/* Create & edit modal */}
      <SidedrawerModal show={modalState.show} closeModal={closeModal}>
        <EmployeeForm
          closeModal={closeModal}
          mode={modalState.mode}
          emp={modalState.emp}
          empList={employees.filter(
            (emp) => emp.employeeID !== modalState?.emp?.employeeID
          )}
          refetchEmps={fetchData}
        />
      </SidedrawerModal>
    </Box>
  );
};

export default Employees;
