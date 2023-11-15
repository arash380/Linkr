import { DEFAULT_API as axios } from "./AxiosInstance";
import { toast } from "react-toastify";
import { _catchAPIError } from "../../utils/helpers";

export const createEmployee = async (e) => {
  const { userName, password, password2, ...emp } = e;

  if (password !== password2) {
    toast.error("Password do not much!");
    throw new Error("Failed!");
  }
  try {
    await axios.post(
      `/employee?userName=${userName}&password=${password}`,
      emp
    );
    toast.success("Employee was created successfully!");
  } catch (err) {
    _catchAPIError(err);
  }
};

export const updateEmployee = async (emp) => {
  try {
    await axios.put("/employee", emp);
    toast.success("Employee details were updated successfully!");
  } catch (err) {
    _catchAPIError(err);
  }
};

export const disableOrEnableEmployee = async (emp) => {
  if (emp.approverID === null) delete emp.approverID;
  if (emp.supervisorID === null) delete emp.supervisorID;

  try {
    await axios.put("/employee", { ...emp, active: !emp.active });
    toast.success("Employee details were updated successfully!");
  } catch (err) {
    _catchAPIError(err);
  }
};
