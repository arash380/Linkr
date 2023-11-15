import { DEFAULT_API as axios } from "./AxiosInstance";
import { toast } from "react-toastify";
import { _catchAPIError } from "../../utils/helpers";

export const createTimesheetRow = async (timesheetRow) => {
  try {
    await axios.post("/timesheet-row", timesheetRow);
    toast.success("Timesheet row was created successfully!");
  } catch (err) {
    _catchAPIError(err);
  }
};

export const updateTimesheetRow = async (timesheetRow) => {
  try {
    await axios.put("/timesheet-row", timesheetRow);
    toast.success("Timesheet row was updated successfully!");
  } catch (err) {
    _catchAPIError(err);
  }
};
