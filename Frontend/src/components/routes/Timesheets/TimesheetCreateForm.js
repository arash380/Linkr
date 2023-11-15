import Heading from "../../UI/typography/Heading";
import { Controller, useForm } from "react-hook-form";
import classes from "../Employees/EmployeeForm.module.css";
import Button from "../../UI/Button/Button";
import { TextField } from "@mui/material";
import { StaticDatePicker } from "@mui/lab";
import moment from "moment";
import { useUser } from "../../../contexts/userContext";
import { createTimesheet } from "../../../services/api/timesheetAxios";

const defaultValues = {
  endDate: new Date(),
};

const TimesheetCreateForm = ({ closeModal, refetchTimesheets }) => {
  const { control, handleSubmit, reset } = useForm({ defaultValues });
  const { user } = useUser();

  const handleUpdateTimesheet = async (timesheet) => {
    try {
      const parsedEndDate = moment(timesheet.endDate).format("YYYY-MM-DD");

      const newTimesheet = {
        endDate: parsedEndDate,
        versionNumber: 1,
        managerApproval: false,
        employeeSignature: null,
        employeeID: user.employeeID,
      };
      await createTimesheet(newTimesheet);

      reset(defaultValues);
      closeModal();
      refetchTimesheets();
    } catch {}
  };

  return (
    <>
      <Heading>Create Timesheet</Heading>
      <form
        className={classes.form}
        onSubmit={handleSubmit(handleUpdateTimesheet)}
      >
        <div className={classes.row}>
          <Controller
            render={({ field: { value, onChange } }) => (
              <StaticDatePicker
                className={classes.datePicker}
                label="End date"
                value={value}
                onChange={(newValue) => {
                  onChange(newValue);
                }}
                renderInput={(params) => <TextField {...params} />}
              />
            )}
            name="endDate"
            control={control}
          />
        </div>

        <Button type="submit">Create</Button>
      </form>
    </>
  );
};

export default TimesheetCreateForm;
