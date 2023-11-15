import Heading from "../../UI/typography/Heading";
import { useForm } from "react-hook-form";
import classes from "../Employees/EmployeeForm.module.css";
import Input from "../../controls/Input";
import AutoComplete from "../../controls/AutoComplete";
import { useEffect } from "react";
import {
  createProject,
  updateProject,
} from "../../../services/api/projectAxios";
import Button from "../../UI/Button/Button";
import { _convertDateForPicker } from "../../../utils/helpers";
import { InputAdornment } from "@mui/material";

export const PROJECT_FORM_MODES = {
  CREATE: "create",
  EDIT: "edit",
};

const defaultValues = {
  projectID: "",
  projectName: "",
  projectStart: _convertDateForPicker(new Date()),
  projectEnd: _convertDateForPicker(new Date()),
  unallocatedBudget: 0,
  projectMarkup: 1,
  projectBudget: 0,
  assistant: null,
  manager: null,
  activeProject: true,
};

const ProjectForm = ({
  mode,
  project,
  closeModal,
  refetchProject,
  empList,
}) => {
  const { control, handleSubmit, reset, watch } = useForm({ defaultValues });

  useEffect(() => {
    if (!project) reset(defaultValues);
    else {
      const { assistantID, employeeID, ...restOfProject } = project;
      const formProject = {
        ...restOfProject,
        assistant: empList.find((e) => e.employeeID === assistantID),
        manager: empList.find((e) => e.employeeID === employeeID),
      };
      reset(formProject);
    }
  }, [empList, project, reset]);

  const handleUpdateProject = async (project) => {
    try {
      const { assistant, manager, projectBudget, ...restOfProject } = project;
      const dbProject = {
        ...restOfProject,
        unallocatedBudget: parseInt(projectBudget),
        projectBudget: parseInt(projectBudget),
        assistantID: assistant?.employeeID,
        employeeID: manager.employeeID,
      };

      if (mode === PROJECT_FORM_MODES.CREATE) await createProject(dbProject);
      else await updateProject(dbProject);

      reset(defaultValues);
      closeModal();
      refetchProject();
    } catch (err) {
      console.error(err);
    }
  };

  const _endDateLaterThanStart = (val) => {
    if (val < watch("projectStart")) {
      return "End Date must be later than the Start Date";
    }
  };

  return (
    <>
      <Heading>
        {mode === PROJECT_FORM_MODES.CREATE ? "Create" : "Edit"} Project
      </Heading>
      <form
        className={classes.form}
        onSubmit={handleSubmit(handleUpdateProject)}
      >
        {mode === PROJECT_FORM_MODES.CREATE && (
          <div className={classes.row}>
            <Input
              name="projectID"
              label="Project ID"
              control={control}
              fullWidth
            />
          </div>
        )}

        <div className={classes.row}>
          <Input
            name="projectName"
            label="Project Name"
            control={control}
            fullWidth
          />
        </div>
        <div className={classes.row}>
          <AutoComplete
            options={empList.filter(
              (e) => watch("assistant")?.employeeID !== e.employeeID
            )}
            name="manager"
            label="Project Manager"
            control={control}
            getOptionLabel={(project) =>
              `${project.firstName} ${project.lastName}`
            }
            required
            isOptionEqualToValue={(op, val) => op.projectID === val.projectID}
            fullWidth
          />
        </div>
        <div className={classes.row}>
          <AutoComplete
            options={empList.filter(
              (e) => watch("manager")?.employeeID !== e.employeeID
            )}
            name="assistant"
            label="Project Assistant"
            control={control}
            getOptionLabel={(project) =>
              `${project.firstName} ${project.lastName}`
            }
            isOptionEqualToValue={(op, val) => op.projectID === val.projectID}
            fullWidth
          />
        </div>
        <div className={classes.row}>
          <Input
            label="Start Date"
            name="projectStart"
            control={control}
            fullWidth
            type="date"
          />
          <Input
            label="End Date"
            name="projectEnd"
            control={control}
            type="date"
            fullWidth
            rules={{ validate: _endDateLaterThanStart }}
          />
        </div>
        <div className={classes.row}>
          {project?.unallocatedBudget === project?.projectBudget && (
            <Input
              name="projectBudget"
              label="Budget"
              control={control}
              type="number"
              fullWidth
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">$</InputAdornment>
                ),
              }}
            />
          )}
        </div>
        <div className={classes.row}>
          <Input
            name="projectMarkup"
            label="Project Markup"
            control={control}
            type="number"
            fullWidth
          />
        </div>

        <Button type="submit">
          {mode === PROJECT_FORM_MODES.CREATE ? "Create" : "Save"}
        </Button>
      </form>
    </>
  );
};

export default ProjectForm;
