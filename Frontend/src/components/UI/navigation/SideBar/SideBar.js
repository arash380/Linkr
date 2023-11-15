import {
  Box,
  Drawer,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  SwipeableDrawer,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import LogoutIcon from "@mui/icons-material/Logout";
import classes from "./SideBar.module.css";
import rp from "../../../routing/routePaths";
import HomeIcon from "@mui/icons-material/Home";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import BackupOutlinedIcon from "@mui/icons-material/BackupOutlined";
import AccountTreeIcon from "@mui/icons-material/AccountTree";
import AccessTimeIcon from "@mui/icons-material/AccessTime";
import PeopleOutlineIcon from "@mui/icons-material/PeopleOutline";
import { useUser } from "../../../../contexts/userContext";

const Sidebar = (props) => {
  const { accountSideBarOpen, setAccountSideBarOpen } = props;
  const navigate = useNavigate();
  const { user, logout } = useUser();

  const sideBarContent = (
    <List className={classes.sideBarContent}>
      <ListItem>
        <ListItemText>
          {user.firstName} {user.lastName}
        </ListItemText>
      </ListItem>

      <ListItem button onClick={() => navigate(rp.home.path)}>
        <ListItemIcon>
          <HomeIcon className={classes.navIcon} />
        </ListItemIcon>
        <ListItemText>{rp.home.name}</ListItemText>
      </ListItem>

      <ListItem button onClick={() => navigate(rp.timesheets.path)}>
        <ListItemIcon>
          <AccessTimeIcon className={classes.navIcon} />
        </ListItemIcon>
        <ListItemText>{rp.timesheets.name}</ListItemText>
      </ListItem>

      <ListItem button onClick={() => navigate(rp.projects.path)}>
        <ListItemIcon>
          <AccountTreeIcon className={classes.navIcon} />
        </ListItemIcon>
        <ListItemText>{rp.projects.name}</ListItemText>
      </ListItem>

      {(user.isAdmin || user.hrEmployee) && (
        <ListItem button onClick={() => navigate(rp.employees.path)}>
          <ListItemIcon>
            <PeopleOutlineIcon className={classes.navIcon} />
          </ListItemIcon>
          <ListItemText>{rp.employees.name}</ListItemText>
        </ListItem>
      )}

      {user.isAdmin && (
        <ListItem button onClick={() => navigate(rp.backup.path)}>
          <ListItemIcon>
            <BackupOutlinedIcon className={classes.navIcon} />
          </ListItemIcon>
          <ListItemText>{rp.backup.name}</ListItemText>
        </ListItem>
      )}

      {!user.isAdmin && (
        <ListItem button onClick={() => navigate(rp.profile.path)}>
          <ListItemIcon>
            <AccountCircleIcon className={classes.navIcon} />
          </ListItemIcon>
          <ListItemText>{rp.profile.name}</ListItemText>
        </ListItem>
      )}

      <ListItem button onClick={logout}>
        <ListItemIcon>
          <LogoutIcon className={classes.navIcon} />
        </ListItemIcon>
        <ListItemText>Sign Out</ListItemText>
      </ListItem>
    </List>
  );

  return (
    <Box component="nav" className={classes.drawer}>
      <SwipeableDrawer
        anchor="left"
        open={accountSideBarOpen}
        classes={{ paper: classes.drawerPaper }}
        className={classes.swipeableSidebar}
        variant="temporary"
        onClose={() => setAccountSideBarOpen(false)}
        onOpen={() => setAccountSideBarOpen(true)}
        ModalProps={{ keepMounted: true }}
      >
        {sideBarContent}
      </SwipeableDrawer>
      <Drawer
        anchor="left"
        variant="permanent"
        classes={{ paper: classes.drawerPaper }}
        className={classes.sidebar}
      >
        {sideBarContent}
      </Drawer>
    </Box>
  );
};

export default Sidebar;
