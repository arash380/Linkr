import { Button as MuiButton } from "@mui/material";
import React from "react";
import EditIcon from "@mui/icons-material/Edit";
import AddIcon from "@mui/icons-material/Add";
import AnalyticsIcon from "@mui/icons-material/Analytics";
import ExpandLessIcon from "@mui/icons-material/ExpandLess";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";

const Button = ({ children, icon, ...otherProps }) => (
  <MuiButton {...otherProps} variant="contained">
    {icon === "edit" && <EditIcon sx={{ width: 16, height: 16, mr: 1 }} />}
    {icon === "add" && <AddIcon sx={{ mr: 1 }} />}
    {icon === "report" && <AnalyticsIcon sx={{ mr: 1 }} />}
    {icon === "less" && <ExpandLessIcon sx={{ mr: 0.3, fontSize: 20 }} />}
    {icon === "more" && <ExpandMoreIcon sx={{ mr: 0.3, fontSize: 20 }} />}
    {children}
  </MuiButton>
);

export default Button;
