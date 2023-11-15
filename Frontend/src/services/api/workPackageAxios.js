import { DEFAULT_API as axios } from "./AxiosInstance";
import { toast } from "react-toastify";
import { _catchAPIError } from "../../utils/helpers";

export const createWorkPackage = async (workPackage) => {
  try {
    await axios.post("/work-package", workPackage);
    toast.success("Work Package was created successfully!");
  } catch (err) {
    _catchAPIError(err);
  }
};

export const updateWorkPackage = async (workPackage) => {
  try {
    await axios.put("/work-package", workPackage);
    toast.success("Work package details were updated successfully!");
  } catch (err) {
    _catchAPIError(err);
  }
};

export const completeWorkPackage = async (workPackage) => {
  try {
    await axios.put("/work-package", {
      ...workPackage,
      completed: true,
    });
    toast.success(`Work package closed successfully!`);
  } catch (err) {
    _catchAPIError(err);
  }
};

export const deleteWorkPackage = async (workPackage) => {
  try {
    await axios.delete(
      `/work-package/?wp-id=${workPackage.workpackageID}&proj-id=${workPackage.projectID}`
    );
    toast.success("Work package was deleted successfully!");
  } catch (err) {
    _catchAPIError(err);
  }
};
