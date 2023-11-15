const rp = {
  wildCard: {
    path: "*",
  },
  login: {
    path: "/login",
    name: "Login",
  },
  home: {
    path: "/",
    name: "Home",
  },
  timesheets: {
    path: "/timesheets",
    name: "Timesheets",
  },
  editTimesheet: {
    path: "/timesheets/:timesheetID",
    name: "Edit Timesheet",
  },
  projects: {
    path: "/projects",
    name: "Projects",
  },
  projectDetails: {
    path: "/projects/:projectID",
    name: "Project Details",
  },
  projectReport: {
    path: "/projects/:projectID/report",
    name: "Project Report",
  },
  createProjects: {
    path: "/projects/create",
    name: "Create Projects",
  },
  workPackageDetails: {
    path: "/projects/:projectID/work-packages/:workpackageID",
    name: "Work Package Details",
  },
  workPackages: {
    path: "/work-packages",
    name: "Work Packages",
  },
  employees: {
    path: "/employees",
    name: "Employees",
  },
  profile: {
    path: "/profile",
    name: "Profile",
  },
  updatePassword: {
    path: "/profile/updatePassword",
    name: "Update Password",
  },
  backup: {
    path: "/backup",
    name: "Backup",
  },
  signOut: {
    path: "/sign-out",
    name: "Sign Out",
  },
};

export default rp;
