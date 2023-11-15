import { DEFAULT_API as axios } from "./AxiosInstance";
import { toast } from "react-toastify";
import { _catchAPIError } from "../../utils/helpers";

export const createTimesheet = async (timesheet) => {
  try {
    await axios.post("/timesheet", timesheet);
    toast.success("Timesheet was created successfully!");
  } catch (err) {
    _catchAPIError(err);
  }
};

export const approveTimesheet = async (timesheet) => {
  try {
    await axios.put("/timesheet", { ...timesheet, managerApproval: true });
    toast.success("Timesheet was approved successfully!");
  } catch (err) {
    _catchAPIError(err);
  }
};
