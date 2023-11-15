import { DEFAULT_API as axios } from "./AxiosInstance";
import { toast } from "react-toastify";
import { _catchAPIError } from "../../utils/helpers";

export const updateProjectEmployees = async (projectID, employeeIDs) => {
  try {
    await axios.put("/project-Employee", {
      projectId: projectID,
      employeeIds: employeeIDs,
    });
    toast.success("Employees updated successfully!");
  } catch (err) {
    _catchAPIError(err);
  }
};
