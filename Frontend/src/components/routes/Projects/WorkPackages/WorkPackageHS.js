import { Stack } from "@mui/material";
import { OBJECT_WILD_CARD_KEY } from "../../../../utils/helpers";
import Button from "../../../UI/Button/Button";

export const WP_UNIQUE_ID = ["projectID", "workpackageID"];

export const WP_HEADERS = (handleView, handleClose, handleDelete, canEdit) => [
  {
    name: "Work Package ID",
    source: WP_UNIQUE_ID[1],
    bold: true,
  },
  {
    name: "Title",
    source: "workPackageTitle",
  },
  {
    name: "Actions",
    source: OBJECT_WILD_CARD_KEY,
    formatter: (wp) => (
      <Stack direction="row" spacing={1}>
        <Button onClick={() => handleView(wp)}>View</Button>

        {canEdit() && (
          <>
            <Button onClick={() => handleClose(wp)} disabled={wp.completed}>
              Close
            </Button>

            <Button onClick={() => handleDelete(wp)} disabled={wp.completed}>
              Delete
            </Button>
          </>
        )}
      </Stack>
    ),
  },
];
