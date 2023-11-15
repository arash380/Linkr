import { OBJECT_WILD_CARD_KEY } from "../../../utils/helpers";
import Button from "../../UI/Button/Button";

export const PROJECT_UNIQUE_ID = "projectID";

export const PROJECT_HEADERS = (handleView, handleClose, canEditProject) => [
  {
    name: "Project ID",
    source: PROJECT_UNIQUE_ID,
    bold: true,
  },
  {
    name: "Title",
    source: "projectName",
  },
  {
    name: "Manager ID",
    source: "employeeID",
  },
  {
    name: "Start Date",
    source: "projectStart",
  },
  {
    name: "Due Date",
    source: "projectEnd",
  },
  {
    name: "Status",
    source: "activeProject",
    formatter: (val) => (val ? "ACTIVE" : "CLOSED"),
  },
  {
    name: "Actions",
    source: OBJECT_WILD_CARD_KEY,
    alignCenter: true,
    formatter: (project) => (
      <div
        style={{
          display: "flex",
          justifyContent: "left",
          gap: "1.0rem",
          marginLeft: "50px",
        }}
      >
        <Button onClick={() => handleView(project?.projectID)}>View</Button>
        {project?.activeProject && canEditProject(project) && (
          <Button onClick={() => handleClose(project)}>Close</Button>
        )}
      </div>
    ),
  },
];
