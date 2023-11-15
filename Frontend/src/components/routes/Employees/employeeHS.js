import {
  DEFAULT_RETURN_VALUE,
  OBJECT_WILD_CARD_KEY,
} from "../../../utils/helpers";
import Button from "../../UI/Button/Button";
import classes from "./Employees.module.css";

export const EMPLOYEE_UNIQUE_ID = "employeeID";

export const EMPLOYEE_HEADERS = (
  handleEdit,
  handleDisableOrEnable,
  empList
) => [
  {
    name: "ID",
    source: EMPLOYEE_UNIQUE_ID,
    bold: true,
  },
  {
    name: "Name",
    source: ["firstName", "lastName"],
  },
  {
    name: "Vacation Days",
    source: "vacationDays",
  },
  {
    name: "Sick Days",
    source: "sickDays",
  },
  {
    name: "Flex Time",
    source: "flexTime",
  },
  {
    name: "Pay Rate",
    source: "payRate",
  },
  {
    name: "HR",
    source: "hrEmployee",
    formatter: (val) => (val ? "Yes" : "No"),
  },
  {
    name: "Supervisor",
    source: "supervisorID",
    formatter: (val) => _getEmpNameById(empList, val),
  },
  {
    name: "Approver",
    source: "approverID",
    formatter: (val) => _getEmpNameById(empList, val),
  },
  {
    name: "Active",
    source: "active",
    formatter: (val) =>
      val ? (
        <span className={classes.active}>Yes</span>
      ) : (
        <span className={classes.inactive}>No</span>
      ),
  },
  {
    name: "Action",
    source: OBJECT_WILD_CARD_KEY,
    alignCenter: true,
    formatter: (emp) => (
      <div className={classes.buttonContainer}>
        <Button onClick={() => handleEdit(emp)}>Edit</Button>
        <Button onClick={() => handleDisableOrEnable(emp)}>
          {emp.active ? "Disable" : "Enable"}
        </Button>
      </div>
    ),
  },
];

// helper
function _getEmpNameById(empList, id) {
  if (id === DEFAULT_RETURN_VALUE) return id;

  const emp = empList.find(({ employeeID }) => employeeID === id);
  return `${emp.firstName} ${emp.lastName}`;
}
