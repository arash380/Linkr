import React, { useRef } from "react";
import { Box, Typography } from "@mui/material";
import useAxiosGet from "../../../hooks/useAxiosGet";
import AppLoader from "../../UI/loading/AppLoader/AppLoader";
import { useParams } from "react-router-dom";
import WPReport from "./WPReport";
import ReportCard from "./ReportCard";
import Heading from "../../UI/typography/Heading";
import Button from "../../UI/Button/Button";

const Reports = () => {
  const { projectID } = useParams();
  const { response, isLoading } = useAxiosGet(`/project/report/${projectID}`);
  const { projectName, projectWorkPackageReportList: reports } = response;
  const totalRef = useRef(null);

  const _getTotalData = () => {
    const res = reports.reduce((accumulator, { parentWorkPackage }) => {
      Object.keys(parentWorkPackage).forEach((key) => {
        accumulator[key] = (accumulator[key] || 0) + parentWorkPackage[key];
      });

      return accumulator;
    }, {});

    res.percentageComplete = res.percentageComplete / reports.length;

    return res;
  };

  const onViewTotal = () => {
    totalRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  if (isLoading) return <AppLoader />;
  return (
    <Box>
      <Typography variant="caption2" sx={{ fontSize: "0.75rem" }}>
        PROJECT REPORT
      </Typography>

      <Box
        display="flex"
        justifyContent="space-between"
        alignItems="flex-start"
        mb={2}
      >
        <Heading>{projectName}</Heading>

        <Button onClick={onViewTotal} icon="report">
          View Total
        </Button>
      </Box>

      {reports.map(
        ({ parentWorkPackage, childrenWorkPackages }, i) =>
          (childrenWorkPackages ||
            parentWorkPackage.workPackageId.endsWith("0000")) && (
            <WPReport
              workPackage={parentWorkPackage}
              childPackages={childrenWorkPackages}
              key={i}
            />
          )
      )}

      <ReportCard data={_getTotalData()} isTotal />
      <div ref={totalRef} />
    </Box>
  );
};

export default Reports;
