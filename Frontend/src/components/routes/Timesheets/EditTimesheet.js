import { useState } from "react";
import { useParams } from "react-router-dom";
import rp from "../../routing/routePaths";
import Table from "../../UI/Table/Table";
import Heading from "../../UI/typography/Heading";
import {
  TIMESHEET_ROW_HEADERS,
  TIMESHEET_ROW_UNIQUE_ID,
} from "./TimesheetRows/timesheetRowHS";
import TimesheetRowForm, {
  TIMESHEET_ROW_FORM_MODES as modes,
} from "./TimesheetRows/TimesheetRowForm";
import { Box, Typography } from "@mui/material";
import Button from "../../UI/Button/Button";
import AddIcon from "@mui/icons-material/Add";
import SidedrawerModal from "../../UI/SidedrawerModal/SidedrawerModal";
import useAxiosGet from "../../../hooks/useAxiosGet";
import { approveTimesheet } from "../../../services/api/timesheetAxios";
import { useUser } from "../../../contexts/userContext";

const DEFAULT_MODAL_STATE = { show: false, mode: null, timesheetRow: null };

const EditTimesheet = () => {
  const { timesheetID } = useParams();
  const { user } = useUser();
  const [modalState, setModalState] = useState(DEFAULT_MODAL_STATE);

  const { response: timesheet, fetchData: fetchTsList } = useAxiosGet(
    `/timesheet/${timesheetID}`
  );

  const { response: timesheetRowList, fetchData: fetchTsRowList } = useAxiosGet(
    `/timesheet-row/?timesheet-id=${timesheetID}`
  );

  const { response: approvees } = useAxiosGet(
    `/employee/?approver-id=${user.employeeID}`,
    !!user
  );

  const onCreateTimesheetRow = () => {
    setModalState({ show: true, mode: modes.CREATE, timesheetRow: null });
  };

  const onEditTimesheetRow = (timesheetRow) => {
    setModalState({
      show: true,
      mode: modes.EDIT,
      timesheetRow: timesheetRowList.find(
        (tr) =>
          tr.projectID === timesheetRow.projectID &&
          tr.workPackageID === timesheetRow.workPackageID
      ),
    });
  };

  const closeModal = () => {
    setModalState(DEFAULT_MODAL_STATE);
  };

  const handleApproveTimesheet = async () => {
    await approveTimesheet(timesheet);
    await fetchTsList();
  };

  const userApprovesTsOwner = approvees?.some(
    (emp) => emp.employeeID === timesheet?.employeeID
  );

  const allowEdit =
    (timesheet.employeeID === user?.employeeID || user.isAdmin) &&
    !timesheet.managerApproval;

  return (
    <>
      <Box
        display="flex"
        justifyContent="space-between"
        alignItems="flex-start"
      >
        <Heading>
          {rp.editTimesheet.name} - {timesheetID}
        </Heading>

        {!timesheet.managerApproval ? (
          <>
            {(user.isAdmin || userApprovesTsOwner) && (
              <Box display="flex" justifyContent="flex-end">
                <Button onClick={handleApproveTimesheet} sx={{ mb: 2 }}>
                  Approve
                </Button>
              </Box>
            )}
          </>
        ) : (
          <Typography color="green">
            <strong>Approved</strong>
          </Typography>
        )}
      </Box>

      {allowEdit && (
        <Box display="flex" justifyContent="flex-end">
          <Button onClick={onCreateTimesheetRow} sx={{ mb: 2 }}>
            <AddIcon />
            <Typography variant="body2" ml={1}>
              Add Timesheet Row
            </Typography>
          </Button>
        </Box>
      )}

      <Table
        data={timesheetRowList}
        headers={TIMESHEET_ROW_HEADERS(onEditTimesheetRow, allowEdit)}
        uniqueIdSrc={TIMESHEET_ROW_UNIQUE_ID}
      />

      {/* Create & edit modal */}
      <SidedrawerModal show={modalState.show} closeModal={closeModal}>
        <TimesheetRowForm
          closeModal={closeModal}
          mode={modalState.mode}
          timesheetRow={modalState.timesheetRow}
          refetchTimesheetRows={fetchTsRowList}
          timesheetID={timesheetID}
        />
      </SidedrawerModal>
    </>
  );
};

export default EditTimesheet;
