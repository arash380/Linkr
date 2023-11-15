import { DEFAULT_API as axios } from "./AxiosInstance";
import { toast } from "react-toastify";
import { _catchAPIError } from "../../utils/helpers";

export const createProjectWpEstimate = async (projectWpEstimate) => {
  try {
    await axios.post("/project-workpackage-estimate", projectWpEstimate);
    toast.success("Effort estimate was created successfully!");
  } catch (err) {
    _catchAPIError(err);
  }
};

export const updateProjectWpEstimate = async (projectWpEstimate) => {
  try {
    await axios.put("/project-workpackage-estimate", projectWpEstimate);
    toast.success("Effort estimate was updated successfully!");
  } catch (err) {
    _catchAPIError(err);
  }
};
