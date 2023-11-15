import { Checkbox } from "@mui/material";
import { OBJECT_WILD_CARD_KEY } from "../../../utils/helpers";
import classes from "./Projects.module.css";

export const EMPLOYEE_ADD_UNIQUE_ID = "employeeID";

export const EMPLOYEE_ADD_HEADERS = (handleToggleAssign, _empIsAssigned) => [
  {
    name: "Employee ID",
    source: EMPLOYEE_ADD_UNIQUE_ID,
    bold: true,
  },
  {
    name: "Name",
    source: ["firstName", "lastName"],
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
