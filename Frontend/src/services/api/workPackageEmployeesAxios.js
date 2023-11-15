import { DEFAULT_API as axios } from "./AxiosInstance";
import { toast } from "react-toastify";
import { _catchAPIError } from "../../utils/helpers";

export const updateWpEmployees = async (
  projectID,
  workPackageID,
  employeeIDs
) => {
  try {
    await axios.put("/workpackage-employee", {
      projectId: projectID,
      workPackageId: workPackageID,
      employeeIds: employeeIDs,
    });
    toast.success("Employees updated successfully!");
  } catch (err) {
    _catchAPIError(err);
  }
};
