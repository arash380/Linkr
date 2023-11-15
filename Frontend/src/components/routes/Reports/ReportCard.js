import React from "react";
import { Box, Card, Stack, Typography } from "@mui/material";
import { formatNumber as format } from "../../../utils/helpers";
import Button from "../../UI/Button/Button";
import classes from "./ReportCard.module.css";

const ReportCard = ({
  data,
  isTotal,
  isChild,
  expanded,
  expandToggle,
  hasExpand,
}) => {
  const {
    actualToDateCost,
    actualToDateDays,
    costEstimatedAtCompletion,
    daysEstimatedAtCompletion,
    engineersEstimatedCost,
    engineersEstimatedDays,
    projectBudgetDays,
    projectBudgetLabourCost,
    workPackageId,
    workPackageName,
    varianceDays,
    varianceCost,
    percentageComplete,
  } = data;

  return (
    <Card
      sx={{ p: 3, mb: 3 }}
      variant="outlined"
      style={
        isTotal
          ? { backgroundColor: "#aca6a4" }
          : isChild
          ? { backgroundColor: "#e1d9d6" }
          : {}
      }
    >
      <Stack direction="column" spacing={2}>
        <Stack
          direction="row"
          spacing={5}
          justifyContent="space-between"
          className={classes.root}
        >
          {!isTotal && (
            <>
              <Box>
                <Typography variant="caption">ID</Typography>
                <Typography variant="h6">{workPackageId}</Typography>
              </Box>
              <Box>
                <Typography variant="caption">Name</Typography>
                <Typography variant="h6">{workPackageName}</Typography>
              </Box>
            </>
          )}
          <Box>
            <Typography variant="caption">
              {isTotal && "Total "}Actual Date Cost
            </Typography>
            <Typography variant="h6">${format(actualToDateCost)}</Typography>
          </Box>
          <Box>
            <Typography variant="caption">
              {isTotal && "Total "}Actual Date Days
            </Typography>
            <Typography variant="h6">{format(actualToDateDays)}</Typography>
          </Box>
          <Box>
            <Typography variant="caption">
              {isTotal && "Total "}Estimated At Completion Cost
            </Typography>
            <Typography variant="h6">
              ${format(costEstimatedAtCompletion)}
            </Typography>
          </Box>
          <Box>
            <Typography variant="caption">
              {isTotal && "Total "}Estimated At Completion Days
            </Typography>
            <Typography variant="h6">
              {format(daysEstimatedAtCompletion)}
            </Typography>
          </Box>
          <Box>
            <Typography variant="caption">
              {isTotal && "Total "}Engineer's Estimated Cost
            </Typography>
            <Typography variant="h6">
              ${format(engineersEstimatedCost)}
            </Typography>
          </Box>
          <Box>
            <Typography variant="caption">
              {isTotal && "Total "}Engineer's Estimated Days
            </Typography>
            <Typography variant="h6">
              {format(engineersEstimatedDays)}
            </Typography>
          </Box>
          <Box>
            <Typography variant="caption">
              {isTotal && "Total "}Budget Days
            </Typography>
            <Typography variant="h6">{format(projectBudgetDays)}</Typography>
          </Box>
          <Box>
            <Typography variant="caption">
              {isTotal && "Total "}Budget Labour Cost
            </Typography>
            <Typography variant="h6">
              {format(projectBudgetLabourCost)}
            </Typography>
          </Box>
          <Box>
            <Typography variant="caption">
              {isTotal && "Total "}Variance Days
            </Typography>
            <Typography variant="h6">{format(varianceDays)}</Typography>
          </Box>
          <Box>
            <Typography variant="caption">
              {isTotal && "Total "}Variance Cost
            </Typography>
            <Typography variant="h6">{format(varianceCost)}</Typography>
          </Box>
          <Box>
            <Typography variant="caption">
              {isTotal && "Total "}Percentage Complete
            </Typography>
            <Typography variant="h6">{format(percentageComplete)}%</Typography>
          </Box>
        </Stack>
      </Stack>

      {hasExpand && !isChild && !isTotal && (
        <Button
          icon={expanded ? "less" : "more"}
          onClick={expandToggle}
          className={classes.expandBtn}
          size="small"
        >
          Expand Work Packages
        </Button>
      )}
    </Card>
  );
};

export default ReportCard;
