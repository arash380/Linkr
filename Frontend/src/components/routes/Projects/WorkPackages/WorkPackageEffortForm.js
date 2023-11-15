import { useForm } from "react-hook-form";
import classes from "../../Employees/EmployeeForm.module.css";
import Input from "../../../controls/Input";
import { useEffect } from "react";
import Button from "../../../UI/Button/Button";
import Heading from "../../../UI/typography/Heading";
import { updateProjectWpEstimate } from "../../../../services/api/projectWorkPackageEstimateAxios";
import {
  createWpEstimateCost,
  updateWpEstimateCost,
} from "../../../../services/api/workPackageEstimateCostsAxios";
import {
  createWpEstimateCostToCompletion,
  updateWpEstimateCostToCompletion,
} from "../../../../services/api/workPackageEstimateToCompletionAxios";

export const WP_EFFORT_FORM_MODES = {
  RES_ENG_CREATE: "res-eng-create",
  RES_ENG_EDIT: "res-eng-edit",
  RES_ENG_CREATE_TO_COMPL: "res-eng-create-to-compl",
  RES_ENG_EDIT_TO_COMPL: "res-eng-edit-to-compl",
  PM_EDIT: "pm-edit",
};

const defaultValues = {
  p1PlannedDays: 0,
  p2PlannedDays: 0,
  p3PlannedDays: 0,
  p4PlannedDays: 0,
  p5PlannedDays: 0,
  jsPlannedDays: 0,
  dsPlannedDays: 0,
  ssPlannedDays: 0,
  percentageComplete: 0,
};

export const POSITIVE_DAYS_RULE = {
  min: { value: 0, message: "Days cannot be negative" },
  pattern: {
    value: /^-?\d*\.?\d+$/,
    message: "Please enter a number",
  },
};

const WorkPackageEffortForm = ({
  mode,
  wpEstimate,
  projectID,
  refetchWpEffortEstimate,
  closeModal,
  workPackageID,
  refetchWpCostEstimate,
  refetchWpCompletionEstimate,
}) => {
  const { control, handleSubmit, reset } = useForm({ defaultValues });

  useEffect(() => {
    if (!wpEstimate) {
      reset(defaultValues);
    } else {
      const { workPackageID, projectID, ...restOfWpEstimate } = wpEstimate;
      reset(restOfWpEstimate);
    }
  }, [reset, wpEstimate]);

  const handleUpdateEstimates = async (estimates) => {
    try {
      Object.entries(estimates).forEach(
        ([key, val]) => (estimates[key] = parseFloat(val))
      );

      const dbEffortEstimates = {
        ...estimates,
        workPackageID,
        projectID,
      };

      if (mode === WP_EFFORT_FORM_MODES.PM_EDIT) {
        await updateProjectWpEstimate(dbEffortEstimates);
        refetchWpEffortEstimate();
      } else if (mode === WP_EFFORT_FORM_MODES.RES_ENG_CREATE) {
        await createWpEstimateCost(dbEffortEstimates);
        refetchWpCostEstimate();
      } else if (mode === WP_EFFORT_FORM_MODES.RES_ENG_EDIT) {
        await updateWpEstimateCost(dbEffortEstimates);
        refetchWpCostEstimate();
      } else if (mode === WP_EFFORT_FORM_MODES.RES_ENG_CREATE_TO_COMPL) {
        await createWpEstimateCostToCompletion(dbEffortEstimates);
        refetchWpCompletionEstimate();
      } else if (mode === WP_EFFORT_FORM_MODES.RES_ENG_EDIT_TO_COMPL) {
        await updateWpEstimateCostToCompletion(dbEffortEstimates);
        refetchWpCompletionEstimate();
      }

      reset(defaultValues);
      closeModal();
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <>
      <Heading>Enter Estimates</Heading>
      <form
        className={classes.form}
        onSubmit={handleSubmit(handleUpdateEstimates)}
      >
        <div className={classes.row}>
          <Input
            name="p1PlannedDays"
            label="P1 Days"
            control={control}
            fullWidth
            rules={POSITIVE_DAYS_RULE}
          />
        </div>
        <div className={classes.row}>
          <Input
            control={control}
            name="p2PlannedDays"
            label="P2 Days"
            fullWidth
            rules={POSITIVE_DAYS_RULE}
          />
        </div>

        <div className={classes.row}>
          <Input
            name="p3PlannedDays"
            label="P3 Days"
            control={control}
            fullWidth
            rules={POSITIVE_DAYS_RULE}
          />
        </div>
        <div className={classes.row}>
          <Input
            name="p4PlannedDays"
            label="P4 Days"
            control={control}
            fullWidth
            rules={POSITIVE_DAYS_RULE}
          />
        </div>
        <div className={classes.row}>
          <Input
            name="p5PlannedDays"
            label="P5 Days"
            control={control}
            fullWidth
            rules={POSITIVE_DAYS_RULE}
          />
        </div>
        <div className={classes.row}>
          <Input
            name="jsPlannedDays"
            label="JS Days"
            control={control}
            fullWidth
            rules={POSITIVE_DAYS_RULE}
          />
        </div>
        <div className={classes.row}>
          <Input
            name="dsPlannedDays"
            label="DS Days"
            control={control}
            fullWidth
            rules={POSITIVE_DAYS_RULE}
          />
        </div>
        <div className={classes.row}>
          <Input
            name="ssPlannedDays"
            label="SS Days"
            control={control}
            fullWidth
            rules={POSITIVE_DAYS_RULE}
          />
        </div>
        {(mode === WP_EFFORT_FORM_MODES.RES_ENG_CREATE_TO_COMPL ||
          mode === WP_EFFORT_FORM_MODES.RES_ENG_EDIT_TO_COMPL) && (
          <Input
            name="percentageComplete"
            label="Percentage Complete"
            control={control}
            fullWidth
            rules={POSITIVE_DAYS_RULE}
          />
        )}

        <Button type="submit">Submit</Button>
      </form>
    </>
  );
};

export default WorkPackageEffortForm;
