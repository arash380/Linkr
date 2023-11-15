import { DEFAULT_API as axios } from "./AxiosInstance";
import { toast } from "react-toastify";
import { _catchAPIError } from "../../utils/helpers";

export const createWpEstimateCost = async (projectWpEstimate) => {
  try {
    await axios.post("/workpackage-estimate-costs", projectWpEstimate);
    toast.success("Effort estimate was created successfully!");
  } catch (err) {
    _catchAPIError(err);
  }
};

export const updateWpEstimateCost = async (projectWpEstimate) => {
  try {
    await axios.put("/workpackage-estimate-costs", projectWpEstimate);
    toast.success("Effort estimate was updated successfully!");
  } catch (err) {
    _catchAPIError(err);
  }
};
