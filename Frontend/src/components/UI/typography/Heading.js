import { Typography } from "@mui/material";

const Heading = ({ children, ...rest }) => {
  return (
    <Typography variant="h4" sx={{ mb: 3 }} {...rest}>
      {children}
    </Typography>
  );
};

export default Heading;
