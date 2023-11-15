import { OBJECT_WILD_CARD_KEY } from "../../../../utils/helpers";
import Button from "../../../UI/Button/Button";
import classes from "../../Employees/Employees.module.css";

export const TIMESHEET_ROW_UNIQUE_ID = ["projectID", "workPackageID"];

export const TIMESHEET_ROW_HEADERS = (handleEdit, allowEdit) => [
  {
    name: "Project",
    source: "projectID",
  },
  {
    name: "Work Package",
    source: "workPackageID",
  },
  {
    name: "Mon",
    source: "hoursInDays",
    formatter: (val) => val[0],
  },
  {
    name: "Tue",
    source: "hoursInDays",
    formatter: (val) => val[1],
  },
  {
    name: "Wed",
    source: "hoursInDays",
    formatter: (val) => val[2],
  },
  {
    name: "Thu",
    source: "hoursInDays",
    formatter: (val) => val[3],
  },
  {
    name: "Fri",
    source: "hoursInDays",
    formatter: (val) => val[4],
  },
  {
    name: "Sat",
    source: "hoursInDays",
    formatter: (val) => val[5],
  },
  {
    name: "Sun",
    source: "hoursInDays",
    formatter: (val) => val[6],
  },
  {
    name: "Action",
    source: OBJECT_WILD_CARD_KEY,
    alignCenter: true,
    formatter: (timesheetRow) => (
      <>
        {allowEdit && (
          <div className={classes.buttonContainer}>
            <Button onClick={() => handleEdit(timesheetRow)}>Edit</Button>
          </div>
        )}
      </>
    ),
  },
];
