import { Checkbox } from "@mui/material";
import { OBJECT_WILD_CARD_KEY } from "../../../../utils/helpers";
import classes from "../Projects.module.css";

export const WP_EMPLOYEES_UNIQUE_ID = "employeeID";

export const WP_EMPLOYEES_HEADERS = (
  handleToggleAssign,
  _empIsAssigned,
  allEmployees
) => [
  {
    name: "Employee ID",
    source: WP_EMPLOYEES_UNIQUE_ID,
    bold: true,
  },
  {
    name: "Name",
    source: OBJECT_WILD_CARD_KEY,
    formatter: (emp) => {
      const completeEmployee = allEmployees.find(
        (e) => e.employeeID === emp.employeeID
      );
      return `${completeEmployee?.firstName} ${completeEmployee?.lastName}`;
    },
  },
  {
    name: "Assign",
    source: OBJECT_WILD_CARD_KEY,
    alignCenter: true,
    formatter: (emp) => (
      <div className={classes.buttonContainer}>
        <Checkbox
          align="center"
          type="checkbox"
          checked={_empIsAssigned(emp)}
          onChange={() => handleToggleAssign(emp)}
        />
      </div>
    ),
  },
];
