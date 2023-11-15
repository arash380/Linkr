import React, { useState } from "react";
import { Box } from "@mui/material";
import Heading from "../../UI/typography/Heading";
import classes from "./Timesheet.module.css";
import { useNavigate } from "react-router-dom";
import rp from "../../routing/routePaths";
import useAxiosGet from "../../../hooks/useAxiosGet";
import { useUser } from "../../../contexts/userContext";
import TimesheetCreateForm from "./TimesheetCreateForm";
import { TIMESHEET_HEADERS, TIMESHEET_UNIQUE_ID } from "./timesheetHS";
import SidedrawerModal from "../../UI/SidedrawerModal/SidedrawerModal";
import Button from "../../UI/Button/Button";
import Table from "../../UI/Table/Table";

const Timesheets = () => {
  const navigate = useNavigate();
  const { user } = useUser();
  const { response: timesheets, fetchData } = useAxiosGet(
    `/timesheet/${!user.isAdmin ? `?emp-id=${user.employeeID}` : ""}`
  );
  const { response: approverTimesheets, error: approverTimesheetsError } =
    useAxiosGet(`/timesheet/approver/${user.employeeID}?only-approved=true`);
  const [showModal, setShowModal] = useState(false);

  const onEditTimesheet = (timesheetID) => {
    navigate(`${rp.timesheets.path}/${timesheetID}`);
  };

  const _openModal = () => setShowModal(true);
  const _closeModal = () => setShowModal(false);

  return (
    <Box>
      <Heading>{rp.timesheets.name}</Heading>

      {/* Admins can not create a timesheet cuz no employee is assigned to them */}
      {!user.isAdmin && (
        <Box display="flex" justifyContent="flex-end">
          <Button
            className={classes.btn}
            onClick={_openModal}
            sx={{ mb: 2 }}
            icon="add"
          >
            Create Timesheet
          </Button>
        </Box>
      )}

      <Table
        data={timesheets}
        headers={TIMESHEET_HEADERS}
        uniqueIdSrc={TIMESHEET_UNIQUE_ID}
        onClick={onEditTimesheet}
      />

      {(user.isAdmin ||
        (!approverTimesheetsError && approverTimesheets?.length > 0)) && (
        <>
          <Heading sx={{ my: 6 }}>Pending Approval</Heading>

          <Table
            data={
              user.isAdmin
                ? timesheets.filter(({ managerApproval }) => !managerApproval)
                : approverTimesheets
            }
            headers={TIMESHEET_HEADERS}
            uniqueIdSrc={TIMESHEET_UNIQUE_ID}
            onClick={onEditTimesheet}
          />
        </>
      )}

      <SidedrawerModal show={showModal} closeModal={_closeModal}>
        <TimesheetCreateForm
          closeModal={_closeModal}
          refetchTimesheets={fetchData}
        />
      </SidedrawerModal>
    </Box>
  );
};

export default Timesheets;
