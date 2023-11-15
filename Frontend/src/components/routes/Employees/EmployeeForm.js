import Heading from "../../UI/typography/Heading";
import { useForm } from "react-hook-form";
import classes from "./EmployeeForm.module.css";
import Input from "../../controls/Input";
import CheckBox from "../../controls/CheckBox";
import AutoComplete from "../../controls/AutoComplete";
import { PAY_RATES } from "./Employees";
import { useEffect } from "react";
import {
  createEmployee,
  updateEmployee,
} from "../../../services/api/employeeAxios";
import Button from "../../UI/Button/Button";

export const EMP_FORM_MODES = {
  CREATE: "create",
  EDIT: "edit",
};

const defaultValues = {
  employeeID: 0,
  active: true,
  firstName: "",
  lastName: "",
  vacationDays: 0,
  flexTime: 0,
  sickDays: 0,
  hrEmployee: false,
  payRate: "JS",
  supervisorID: null,
  approverID: null,
  salariedEmployee: false,
  userName: "",
  password: "",
  password2: "",
};

const EmployeeForm = ({ mode, emp, closeModal, refetchEmps, empList }) => {
  const { control, handleSubmit, reset, watch } = useForm({ defaultValues });

  useEffect(() => {
    if (!emp) reset(defaultValues);
    else reset(emp);
  }, [emp, reset]);

  const handleUpdateEmp = async (emp) => {
    if (emp.approverID === null) delete emp.approverID;
    if (emp.supervisorID === null) delete emp.supervisorID;

    try {
      if (mode === EMP_FORM_MODES.CREATE) await createEmployee(emp);
      else await updateEmployee(emp);

      reset(defaultValues);
      closeModal();
      refetchEmps();
    } catch {}
  };

  const _getAutocompleteOptions = () => {
    return empList
      ?.filter((emp) => emp.employeeID !== watch().employeeID)
      .map(({ employeeID }) => employeeID);
  };

  const _getAutocompleteLabel = (id) => {
    const emp = empList.find(({ employeeID }) => id === employeeID);
    return `${emp.firstName} ${emp.lastName}`;
  };

  return (
    <>
      <Heading data-testid="test1">
        {mode === EMP_FORM_MODES.CREATE ? "Create" : "Edit"} Employee
      </Heading>
      <form className={classes.form} onSubmit={handleSubmit(handleUpdateEmp)}>
        {mode === EMP_FORM_MODES.CREATE && (
          <Input
            name="employeeID"
            label="ID"
            type="number"
            inputProps={{ min: 0 }}
            control={control}
            fullWidth
          />
        )}
        <div className={classes.row}>
          <Input
            name="firstName"
            label="First Name"
            control={control}
            fullWidth
          />
          <Input
            name="lastName"
            label="Last Name"
            control={control}
            fullWidth
          />
        </div>
        {mode === EMP_FORM_MODES.CREATE && (
          <>
            <Input
              name="userName"
              label="Username"
              control={control}
              fullWidth
            />
            <div className={classes.row}>
              <Input
                name="password"
                label="Password"
                type="password"
                control={control}
                fullWidth
              />
              <Input
                name="password2"
                label="Confirm Password"
                type="password"
                control={control}
                fullWidth
              />
            </div>
          </>
        )}
        <div className={classes.row}>
          <CheckBox name="hrEmployee" label="HR Employee" control={control} />
          <CheckBox
            name="salariedEmployee"
            label="Salaried Employee"
            control={control}
          />
        </div>
        <AutoComplete
          options={PAY_RATES}
          name="payRate"
          label="Pay Rate"
          control={control}
          fullWidth
        />
        <div className={classes.row}>
          <AutoComplete
            options={_getAutocompleteOptions()}
            name="supervisorID"
            label="Supervisor"
            control={control}
            getOptionLabel={_getAutocompleteLabel}
            fullWidth
          />
          <AutoComplete
            options={_getAutocompleteOptions()}
            name="approverID"
            label="Approver"
            control={control}
            getOptionLabel={_getAutocompleteLabel}
            fullWidth
          />
        </div>

        <Button type="submit">
          {mode === EMP_FORM_MODES.CREATE ? "Create" : "Save"}
        </Button>
      </form>
    </>
  );
};

export default EmployeeForm;
