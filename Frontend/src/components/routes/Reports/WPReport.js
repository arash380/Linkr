import React, { useState } from "react";
import ReportCard from "./ReportCard";

const WPReport = ({ workPackage, childPackages, isChild, show = true }) => {
  const [showChilds, setShowChilds] = useState(false);

  return (
    <>
      {show && (
        <ReportCard
          data={workPackage}
          isChild={isChild}
          expanded={showChilds}
          hasExpand={childPackages}
          expandToggle={() => setShowChilds((v) => !v)}
        />
      )}

      {childPackages?.map((r, i) => (
        <WPReport workPackage={r} key={i} show={showChilds} isChild />
      ))}
    </>
  );
};

export default WPReport;
