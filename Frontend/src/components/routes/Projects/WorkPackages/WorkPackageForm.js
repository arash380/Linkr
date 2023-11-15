import { useForm } from "react-hook-form";
import classes from "../../Employees/EmployeeForm.module.css";
import Input from "../../../controls/Input";
import AutoComplete from "../../../controls/AutoComplete";
import { useEffect } from "react";
import {
  createWorkPackage,
  updateWorkPackage,
} from "../../../../services/api/workPackageAxios";
import Button from "../../../UI/Button/Button";
import Heading from "../../../UI/typography/Heading";
import { _convertDateForPicker, _getWpPrefix } from "../../../../utils/helpers";
import CheckBox from "../../../controls/CheckBox";
import { POSITIVE_DAYS_RULE } from "./WorkPackageEffortForm";
import { InputAdornment, Typography } from "@mui/material";
import { createProjectWpEstimate } from "../../../../services/api/projectWorkPackageEstimateAxios";

export const WP_FORM_MODES = {
  CREATE: "create",
  EDIT: "edit",
};

const wpEstimates = {
  p1PlannedDays: 0,
  p2PlannedDays: 0,
  p3PlannedDays: 0,
  p4PlannedDays: 0,
  p5PlannedDays: 0,
  jsPlannedDays: 0,
  dsPlannedDays: 0,
};

const defaultValues = {
  workpackageID: "",
  workPackageTitle: "",
  startingBudget: 0,
  chargable: false,
  workpackageStartDate: _convertDateForPicker(new Date()),
  workpackageEndDate: _convertDateForPicker(new Date()),
  responsibleEngineer: null,
  ...wpEstimates,
};

const WorkPackageForm = ({
  mode,
  wp,
  closeModal,
  refetchWorkPackages,
  refetchParent,
  empList,
  projectID,
  parentWpID,
}) => {
  const { control, handleSubmit, reset } = useForm({ defaultValues });

  useEffect(() => {
    if (!wp) {
      const formWP = defaultValues;
      if (parentWpID) {
        formWP.workpackageID = _getWpPrefix(parentWpID);
      }
      reset(formWP);
    } else {
      const { responsibleEngineerID, ...restOfWP } = wp;
      const formWP = {
        ...restOfWP,
        responsibleEngineer: empList.find(
          (e) => e.employeeID === responsibleEngineerID
        ),
      };

      reset(formWP);
    }
  }, [empList, wp, reset, parentWpID]);

  const _addZeroes = (wpID) => {
    return wpID.padEnd(5, 0);
  };

  const handleUpdateWP = async (wp) => {
    try {
      const {
        responsibleEngineer,
        workpackageID,
        p1PlannedDays,
        p2PlannedDays,
        p3PlannedDays,
        p4PlannedDays,
        p5PlannedDays,
        jsPlannedDays,
        dsPlannedDays,
        ...restOfWp
      } = wp;

      const formattedWpID = _addZeroes(workpackageID).toUpperCase();

      const dbWorkPackage = {
        ...restOfWp,
        startingBudget: parseInt(restOfWp.startingBudget),
        workpackageID: formattedWpID,
        projectID: projectID,
        responsibleEngineerID: responsibleEngineer.employeeID,
      };

      const dbEffortEstimates = {
        p1PlannedDays,
        p2PlannedDays,
        p3PlannedDays,
        p4PlannedDays,
        p5PlannedDays,
        jsPlannedDays,
        dsPlannedDays,
        workPackageID: formattedWpID,
        projectID: projectID,
      };

      if (mode === WP_FORM_MODES.CREATE) {
        await createWorkPackage(dbWorkPackage);
        await createProjectWpEstimate(dbEffortEstimates);
        refetchWorkPackages();
      } else {
        await updateWorkPackage(dbWorkPackage);
      }

      refetchParent();
      reset(defaultValues);
      closeModal();
    } catch (err) {
      console.error(err);
    }
  };

  const _validateWp = (wpID) => {
    const prefix = _getWpPrefix(parentWpID);

    if (parentWpID) {
      if (wpID.length !== prefix.length + 1) {
        return `Work Package ID must have a length of ${prefix.length + 1}`;
      }
      if (!wpID.toUpperCase().startsWith(prefix))
        return `The new Work Package ID must begin with the current Work Package ID: ${prefix}`;
      if (wpID[prefix.length] === 0 || wpID[prefix.length] === "0")
        return "The new character cannot be a zero";
    } else if (wpID === 0 || wpID === "0") {
      return "Work Package ID cannot begin with a zero";
    }

    if (!wpID.match(/^[0-9a-z]+$/i)) {
      return "Work Package ID must only contain letters and numbers";
    }

    return true;
  };

  return (
    <>
      <Heading>
        {mode === WP_FORM_MODES.CREATE ? "Create" : "Edit"} Work Package
      </Heading>
      <form className={classes.form} onSubmit={handleSubmit(handleUpdateWP)}>
        {mode === WP_FORM_MODES.CREATE && (
          <div className={classes.row}>
            <Input
              name="workpackageID"
              label="Work Package ID"
              control={control}
              inputProps={{
                maxLength: parentWpID
                  ? Math.min(_getWpPrefix(parentWpID)?.length + 1, 5)
                  : 1,
                style: { textTransform: "uppercase" },
              }}
              rules={{
                validate: _validateWp,
              }}
              fullWidth
            />
          </div>
        )}

        <div className={classes.row}>
          <Input
            name="workPackageTitle"
            label="Title"
            control={control}
            fullWidth
          />
        </div>
        <div className={classes.row}>
          <Input
            name="startingBudget"
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
        </div>

        <div className={classes.row}>
          <Input
            label="Start Date"
            name="workpackageStartDate"
            control={control}
            fullWidth
            type="date"
          />
          <Input
            label="End Date"
            name="workpackageEndDate"
            control={control}
            type="date"
            fullWidth
          />
        </div>
        <div className={classes.row}>
          <AutoComplete
            options={empList}
            name="responsibleEngineer"
            label="Responsible Engineer"
            control={control}
            getOptionLabel={(emp) => `${emp.firstName} ${emp.lastName}`}
            isOptionEqualToValue={(op, val) => op.employeeID === val.employeeID}
            fullWidth
            required
          />
        </div>
        <CheckBox name="chargable" label="Chargeable" control={control} />
        {mode === WP_FORM_MODES.CREATE && (
          <>
            <Typography variant="h6" sx={{ mt: 2 }}>
              Effort Estimates
            </Typography>
            <div className={classes.row}>
              <Input
                name="p1PlannedDays"
                label="P1 Days"
                control={control}
                type="number"
                fullWidth
                rules={POSITIVE_DAYS_RULE}
              />
            </div>
            <div className={classes.row}>
              <Input
                control={control}
                name="p2PlannedDays"
                label="P2 Days"
                type="number"
                fullWidth
                rules={POSITIVE_DAYS_RULE}
              />
            </div>

            <div className={classes.row}>
              <Input
                name="p3PlannedDays"
                label="P3 Days"
                control={control}
                type="number"
                fullWidth
                rules={POSITIVE_DAYS_RULE}
              />
            </div>
            <div className={classes.row}>
              <Input
                name="p4PlannedDays"
                label="P4 Days"
                control={control}
                type="number"
                fullWidth
                rules={POSITIVE_DAYS_RULE}
              />
            </div>
            <div className={classes.row}>
              <Input
                name="p5PlannedDays"
                label="P5 Days"
                control={control}
                type="number"
                fullWidth
                rules={POSITIVE_DAYS_RULE}
              />
            </div>
            <div className={classes.row}>
              <Input
                name="jsPlannedDays"
                label="JS Days"
                control={control}
                type="number"
                fullWidth
                rules={POSITIVE_DAYS_RULE}
              />
            </div>
            <div className={classes.row}>
              <Input
                name="dsPlannedDays"
                label="DS Days"
                control={control}
                type="number"
                fullWidth
                rules={POSITIVE_DAYS_RULE}
              />
            </div>
          </>
        )}

        <Button type="submit">
          {mode === WP_FORM_MODES.CREATE ? "Create" : "Save"}
        </Button>
      </form>
    </>
  );
};

export default WorkPackageForm;
