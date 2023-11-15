import { DEFAULT_API as axios } from "./AxiosInstance";
import { toast } from "react-toastify";
import { _catchAPIError } from "../../utils/helpers";

export const login = async (username, password) => {
  try {
    const r = await axios.get(`/credentials`, {
      params: { username, password },
    });
    return r.headers.token;
  } catch (err) {
    _catchAPIError(err);
  }

  return null;
};

export const updatePassword = async (employeeID, userName, oPass, nPass) => {
  try {
    await axios.get("/credentials", {
      params: { username: userName, password: oPass },
    });

    await axios.put("/credentials", { employeeID, password: nPass, userName });
    toast.success("Password was updated successfully!");
  } catch (err) {
    _catchAPIError(err);
  }
};
