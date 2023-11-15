import { DEFAULT_API as axios } from "./AxiosInstance";
import { toast } from "react-toastify";
import { _catchAPIError } from "../../utils/helpers";

export const createProject = async (project) => {
  try {
    await axios.post("/project", project);
    toast.success("Project was created successfully!");
  } catch (err) {
    _catchAPIError(err);
  }
};

export const updateProject = async (project) => {
  try {
    await axios.put("/project", project);
    toast.success("Project details were updated successfully!");
  } catch (err) {
    _catchAPIError(err);
  }
};

export const toggleProjectActive = async (project) => {
  try {
    await axios.put("/project", {
      ...project,
      activeProject: !project.activeProject,
    });
    toast.success(
      `Project ${project.activeProject ? "closed" : "open"} successfully!`
    );
  } catch (err) {
    _catchAPIError(err);
  }
};
