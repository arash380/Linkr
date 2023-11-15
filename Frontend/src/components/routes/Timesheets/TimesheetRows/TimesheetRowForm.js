import Heading from "../../../UI/typography/Heading";
import { useForm } from "react-hook-form";
import classes from "../../Employees/EmployeeForm.module.css";
import { useEffect } from "react";
import Button from "../../../UI/Button/Button";
import AutoComplete from "../../../controls/AutoComplete";
import TextInput from "../../../controls/Input";
import useAxiosGet from "../../../../hooks/useAxiosGet";
import {
  createTimesheetRow,
  updateTimesheetRow,
} from "../../../../services/api/timesheetRowAxios";
import { useUser } from "../../../../contexts/userContext";
export const TIMESHEET_ROW_FORM_MODES = {
  CREATE: "create",
  EDIT: "edit",
};

const WEEK_DAYS = { MON: 0, TUE: 1, WED: 2, THU: 3, FRI: 4, SAT: 5, SUN: 6 };

const defaultValues = {
  project: null,
  workPackage: null,
  mon: 0,
  tue: 0,
  wed: 0,
  thu: 0,
  fri: 0,
  sat: 0,
  sun: 0,
};

const POSITIVE_HOURS_RULE = {
  min: { value: 0, message: "Hours cannot be negative" },
};

const POSITIVE_HOURS_PROP = { min: 0 };

const TimesheetRowForm = ({
  mode,
  timesheetRow,
  closeModal,
  refetchTimesheetRows,
  timesheetID,
}) => {
  const { user } = useUser();
  const {
    control,
    handleSubmit,
    reset,
    watch,
    setValue,
    setError,
    clearErrors,
  } = useForm({
    defaultValues,
  });

  const selectedProject = watch("project");

  const { response: projectList } = useAxiosGet(`/project`);
  const {
    response: workPackageList,
    error: workPackagesError,
    isLoading: workPackagesLoading,
  } = useAxiosGet(
    `/work-package/?proj-id=${selectedProject?.projectID}`,
    selectedProject ? true : false
  );

  const { response: employeeProjects } = useAxiosGet(
    `project-Employee/?employeeID=${user.employeeID}`
  );

  const { response: employeeWps } = useAxiosGet(
    `workpackage-employee/?employeeID=${user.employeeID}`
  );

  const employeeProjectList = projectList.filter((p) =>
    employeeProjects.some((ep) => ep.projectID === p.projectID)
  );

  const employeeWorkPackageList = workPackageList.filter((wp) =>
    employeeWps.some((wpm) => wpm.workPackageID === wp.workpackageID)
  );

  const chargeableEmpWpList = employeeWorkPackageList.filter(
    (wp) => wp.chargable
  );

  const handleUpdateTimesheetRow = async (newTimesheetRow) => {
    try {
      const { mon, tue, wed, thu, fri, sat, sun, project, workPackage } =
        newTimesheetRow;

      const formattedTimesheetRow = {
        hoursInDays: [
          parseInt(mon),
          parseInt(tue),
          parseInt(wed),
          parseInt(thu),
          parseInt(fri),
          parseInt(sat),
          parseInt(sun),
        ],
        timesheetID: parseInt(timesheetID),
      };

      if (mode === TIMESHEET_ROW_FORM_MODES.CREATE) {
        const newTimesheetRow = {
          ...formattedTimesheetRow,
          workPackageID: workPackage.workpackageID,
          projectID: project.projectID,
        };
        await createTimesheetRow(newTimesheetRow);
      } else {
        const updatedTimesheetRow = {
          ...formattedTimesheetRow,
          workPackageID: timesheetRow.workPackageID,
          projectID: timesheetRow.projectID,
        };
        await updateTimesheetRow(updatedTimesheetRow);
      }

      reset(defaultValues);
      closeModal();
      refetchTimesheetRows();
    } catch {}
  };

  useEffect(() => {
    if (!timesheetRow) {
      reset(defaultValues);
    } else {
      const formTimesheetRow = {
        mon: timesheetRow.hoursInDays[WEEK_DAYS.MON],
        tue: timesheetRow.hoursInDays[WEEK_DAYS.TUE],
        wed: timesheetRow.hoursInDays[WEEK_DAYS.WED],
        thu: timesheetRow.hoursInDays[WEEK_DAYS.THU],
        fri: timesheetRow.hoursInDays[WEEK_DAYS.FRI],
        sat: timesheetRow.hoursInDays[WEEK_DAYS.SAT],
        sun: timesheetRow.hoursInDays[WEEK_DAYS.SUN],
      };

      reset(formTimesheetRow);
    }
  }, [timesheetRow, reset, mode]);

  useEffect(() => {
    setValue("workPackage", null);
  }, [selectedProject, setValue]);

  useEffect(() => {
    if (!selectedProject) return;
    console.log(workPackagesError);
    console.log(chargeableEmpWpList);
    if (
      workPackagesError ||
      (!workPackagesLoading && chargeableEmpWpList.length < 1)
    ) {
      setError("workPackage", {
        type: "manual",
        message:
          "Could not find any chargeable work packages for the selected project",
      });
    } else {
      clearErrors("workPackage");
    }
  }, [
    clearErrors,
    setError,
    workPackagesError,
    chargeableEmpWpList,
    selectedProject,
    workPackagesLoading,
  ]);

  return (
    <>
      <Heading>
        {mode === TIMESHEET_ROW_FORM_MODES.CREATE ? "Create " : "Edit "}
        Timesheet Row
      </Heading>
      <form
        className={classes.form}
        onSubmit={handleSubmit(handleUpdateTimesheetRow)}
      >
        <div className={classes.row}>
          <AutoComplete
            options={employeeProjectList}
            getOptionLabel={(op) => op.projectID}
            isOptionEqualToValue={(op, val) => op.projectID === val.projectID}
            name="project"
            label="Project"
            control={control}
            rules={{
              required: {
                value: timesheetRow ? false : true,
                message: "Must select a project",
              },
            }}
            fullWidth
            hidden={!!timesheetRow}
            required={timesheetRow ? false : true}
          />
        </div>
        <div className={classes.row}>
          <AutoComplete
            options={chargeableEmpWpList}
            getOptionLabel={(op) => op.workpackageID}
            isOptionEqualToValue={(op, val) =>
              op.workPackageID === val.workPackageID
            }
            name="workPackage"
            label="Work Package"
            control={control}
            rules={{
              required: {
                value: timesheetRow ? false : true,
                message: "Must select a work package",
              },
            }}
            fullWidth
            required={timesheetRow ? false : true}
            disabled={Boolean(
              !watch("project") ||
                chargeableEmpWpList.length <= 0 ||
                workPackagesError
            )}
            hidden={!!timesheetRow}
          />
        </div>

        <div className={classes.row}>
          <TextInput
            control={control}
            label="Monday"
            name="mon"
            type="number"
            rules={POSITIVE_HOURS_RULE}
            fullWidth
            inputProps={POSITIVE_HOURS_PROP}
          />
        </div>
        <div className={classes.row}>
          <TextInput
            control={control}
            label="Tuesday"
            name="tue"
            type="number"
            rules={POSITIVE_HOURS_RULE}
            fullWidth
            inputProps={POSITIVE_HOURS_PROP}
          />
        </div>
        <div className={classes.row}>
          <TextInput
            control={control}
            label="Wednesday"
            name="wed"
            type="number"
            rules={POSITIVE_HOURS_RULE}
            fullWidth
            inputProps={POSITIVE_HOURS_PROP}
          />
        </div>
        <div className={classes.row}>
          <TextInput
            control={control}
            label="Thursday"
            name="thu"
            type="number"
            rules={POSITIVE_HOURS_RULE}
            fullWidth
            inputProps={POSITIVE_HOURS_PROP}
          />
        </div>
        <div className={classes.row}>
          <TextInput
            control={control}
            label="Friday"
            name="fri"
            type="number"
            rules={POSITIVE_HOURS_RULE}
            fullWidth
            inputProps={POSITIVE_HOURS_PROP}
          />
        </div>
        <div className={classes.row}>
          <TextInput
            control={control}
            label="Saturday"
            name="sat"
            type="number"
            rules={POSITIVE_HOURS_RULE}
            fullWidth
            inputProps={POSITIVE_HOURS_PROP}
          />
        </div>
        <div className={classes.row}>
          <TextInput
            control={control}
            label="Sunday"
            name="sun"
            type="number"
            rules={POSITIVE_HOURS_RULE}
            fullWidth
            inputProps={POSITIVE_HOURS_PROP}
          />
        </div>

        <Button type="submit">
          {mode === TIMESHEET_ROW_FORM_MODES.CREATE ? "Create" : "Save"}
        </Button>
      </form>
    </>
  );
};

export default TimesheetRowForm;
