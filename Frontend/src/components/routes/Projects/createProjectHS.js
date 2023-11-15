import { OBJECT_WILD_CARD_KEY } from "../../../utils/helpers";

export const EMPLOYEE_UNIQUE_ID = "employeeID";

export const EMPLOYEE_HEADERS = (empList) => [
  {
    name: "Employee ID",
    source: EMPLOYEE_UNIQUE_ID,
    bold: true,
  },
  {
    name: "Name",
    source: OBJECT_WILD_CARD_KEY,
    formatter: (emp) => {
      const completeEmployee = empList.find(
        (e) => e.employeeID === emp.employeeID
      );
      return `${completeEmployee?.firstName} ${completeEmployee?.lastName}`;
    },
  },
];
