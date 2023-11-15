import React from "react";
import { Box } from "@mui/material";
import Heading from "../../UI/typography/Heading";
import { useUser } from "../../../contexts/userContext";

const Home = () => {
  const { user } = useUser();

  return (
    <Box>
      <Heading>
        Welcome, {user.firstName} {user.lastName}
      </Heading>
    </Box>
  );
};

export default Home;
