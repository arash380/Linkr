import { MoonLoader } from "react-spinners";
import useTheme from "@mui/material/styles/useTheme";
import { Box } from "@mui/material";
import classes from "./AppLoader.module.css";

const AppLoader = ({ centerInParent }) => {
  const theme = useTheme();

  return (
    <Box className={centerInParent ? classes.centerInParent : classes.root}>
      <MoonLoader color={theme?.palette?.primary?.main} />
    </Box>
  );
};

export default AppLoader;
