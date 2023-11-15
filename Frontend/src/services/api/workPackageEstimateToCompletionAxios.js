import { DEFAULT_API as axios } from "./AxiosInstance";
import { toast } from "react-toastify";
import { _catchAPIError } from "../../utils/helpers";

export const createWpEstimateCostToCompletion = async (projectWpEstimate) => {
  try {
    await axios.post("/workpackage-estimate-to-completion", projectWpEstimate);
    toast.success("Effort estimate was created successfully!");
  } catch (err) {
    _catchAPIError(err);
  }
};

export const updateWpEstimateCostToCompletion = async (projectWpEstimate) => {
  try {
    await axios.put("/workpackage-estimate-to-completion", projectWpEstimate);
    toast.success("Effort estimate was updated successfully!");
  } catch (err) {
    _catchAPIError(err);
  }
};
