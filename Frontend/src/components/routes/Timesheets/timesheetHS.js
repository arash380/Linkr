import moment from "moment";

export const TIMESHEET_UNIQUE_ID = "timesheetID";

export const TIMESHEET_HEADERS = [
  {
    name: "Timesheet ID",
    source: TIMESHEET_UNIQUE_ID,
    bold: true,
  },
  {
    name: "Version Number",
    source: "versionNumber",
  },
  {
    name: "End Date",
    source: "endDate",
    formatter: (val) => moment(val).format("MMM D YYYY"),
  },
  {
    name: "Approval",
    source: "managerApproval",
    formatter: (val) => (val ? "Yes" : "No"),
  },
  {
    name: "Employee Signature",
    source: "employeeSignature",
  },
];
