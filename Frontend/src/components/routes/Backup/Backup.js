import React from "react";
import { Box } from "@mui/material";
import Heading from "../../UI/typography/Heading";
import BackupIcon from "@mui/icons-material/Backup";
import classes from "./Backup.module.css";

const Backup = () => {
  return (
    <Box className={classes.root}>
      <Heading>Backup</Heading>

      <p>
        To back up the database, we will use the "mysqldump" command.
        <br /> Run this following line to backup the database:
        <code>
          {
            "Mysqldump –user root –password [Enter Password] [Database Name] < [New_Database_Name].sql"
          }
        </code>
        <br />
        This command will back up a single database into a SQL file. The two
        advantages to backing up an SQL file is that you are able to restore the
        file in a different version of MySQL, allowing easy updates. The other
        advantage is being able to edit the .sql file manually before restoring
        the file.
      </p>
      <BackupIcon className={classes.icon} />
    </Box>
  );
};
export default Backup;
