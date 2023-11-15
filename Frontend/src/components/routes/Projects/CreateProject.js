import { Button } from "@mui/material";
import { Box } from "@mui/system";
import Heading from "../../UI/typography/Heading";
import { useForm } from "react-hook-form";
import classes from "./CreateProjectForm.module.css";
import Input from "../../controls/Input";
import { useState } from "react";
import React from "react";
import "reactjs-popup/dist/index.css";
import TableComp from "../../UI/Table/Table";
import { EMPLOYEE_HEADERS, EMPLOYEE_UNIQUE_ID } from "./createProjectHS";
import { EMPLOYEE_ADD_HEADERS, EMPLOYEE_ADD_UNIQUE_ID } from "./AddEmployeeHS";
import SidedrawerModal from "../../UI/SidedrawerModal/SidedrawerModal";

const employeeArray = [
  {
    employeeID: 0,
    firstName: "Michael",
    lastName: "Scott",
    role: "Software Developer",
  },
  {
    employeeID: 1,
    firstName: "Jim",
    lastName: "Halpert",
    role: "Software Developer",
  },
  {
    employeeID: 2,
    firstName: "Dwight",
    lastName: "Schrute",
    role: "Tester",
  },
  {
    employeeID: 3,
    firstName: "Stanley",
    lastName: "Hudson",
    role: "Technical Writer",
  },
  {
    employeeID: 4,
    firstName: "Creed",
    lastName: "Creed",
    role: "QA Officer",
  },
  {
    employeeID: 5,
    firstName: "Toby",
    lastName: "Flenderson",
    role: "Database Administrator",
  },
  {
    employeeID: 6,
    firstName: "Kevin",
    lastName: "Malone",
    role: "Business Analyst",
  },
  {
    employeeID: 7,
    firstName: "Oscar",
    lastName: "Martinez",
    role: "UI Specialist",
  },
];

const DEFAULT_MODAL_STATE = { show: false };

const CreateProjectForm = ({ mode, project }) => {
  const { control, handleSubmit } = useForm();
  var [availableEmps, setAvailableEmps] = useState(employeeArray);
  var [employeesList, setEmployeesList] = useState([]);
  const [modalState, setModalState] = useState(DEFAULT_MODAL_STATE);

  function findIndexOfElement(arr, row) {
    for (let i = 0; i < arr.length; i++) {
      if (arr[i] === row) {
        return i;
      }
    }
    return -1;
  }

  function updateEmployeesAndAvailableList(employeesList, availableEmps, emp) {
    let index = findIndexOfElement(availableEmps, emp);
    if (index !== -1) {
      availableEmps.splice(index, 1);
      setEmployeesList([...employeesList, emp]);
      setAvailableEmps(availableEmps);
    } else {
      let index2 = findIndexOfElement(employeesList, emp);
      employeesList.splice(index2, 1);
      setAvailableEmps([...availableEmps, emp]);
      setEmployeesList(employeesList);
    }
  }

  function removeFromProjectList(row) {
    let index = findIndexOfElement(employeesList, row);
    employeesList.splice(index, 1);
    availableEmps.push(row);
    setAvailableEmps(availableEmps);
    setEmployeesList(employeesList);
  }

  const handleCreateProject = async (project) => {
    // try {
    //    await createProject(emp);
    //   reset();
    // } catch {}
    console.log(project);
    console.log(employeesList);
  };

  const onShowEmp = (emp) => {
    console.log(emp);
    setModalState({ show: true });
  };

  const closeModal = () => setModalState(DEFAULT_MODAL_STATE);

  return (
    <Box>
      <Heading>Create Project</Heading>
      <form
        className={classes.form}
        onSubmit={handleSubmit(handleCreateProject)}
      >
        <Input
          name="projectId"
          label="Project ID"
          maxLength="4"
          minLength="4"
          control={control}
          fullWidth
        />
        <Input
          name="projectManagerId"
          label="Project Manager ID"
          control={control}
          fullWidth
        />
        <Input
          name="projectName"
          label="Project Name"
          control={control}
          fullWidth
        />
        <div>
          Project Start Date{" "}
          <Input name="projectStartDate" control={control} type="date" />
        </div>
        <div>
          Project End Date{" "}
          <Input name="projectEndDate" control={control} type="date" />
        </div>
        <Input
          name="projectBudger"
          label="Project Budget"
          control={control}
          type="number"
          fullWidth
        />
        <Input
          name="projectDesc"
          label="Project Description"
          control={control}
          type="textarea"
          fullWidth
        />
        <output>Select Employees for New Project: </output>
        <Button className={classes.btn} onClick={onShowEmp}>
          Add Employees
        </Button>

        <SidedrawerModal show={modalState.show} closeModal={closeModal}>
          <TableComp
            data={availableEmps}
            headers={EMPLOYEE_ADD_HEADERS(
              updateEmployeesAndAvailableList,
              employeesList,
              availableEmps
            )}
            uniqueIdSrc={EMPLOYEE_ADD_UNIQUE_ID}
          />
          <Button className={classes.doneButton} onClick={closeModal}>
            Done
          </Button>
        </SidedrawerModal>

        <TableComp
          data={employeesList}
          headers={EMPLOYEE_HEADERS(removeFromProjectList)}
          uniqueIdSrc={EMPLOYEE_UNIQUE_ID}
          className={classes.empTable}
        />
        <Button type="submit" onClick={handleCreateProject}>
          Save
        </Button>
      </form>
    </Box>
  );
};

export default CreateProjectForm;
