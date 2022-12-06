# GameOfLife
The goal of this application is to simulate a cells population able to reproduce and eat. 

# Functional requirements 
1. The system shall be able to add the number of sexual and asexual cells.
2. The system shall be able to add the number of food resources.
3 The system shall be able to add the period of time in which a cell is not hungry (T_full).
4. The system shall be able to add the period of time in which a cell is hungry (T_starve).
5. The system shall be able to kill a cell if it isn't fed in T_starve period.
6. The system shall be able to create 1-5 new food resources after a cell dies.
7. After a cell eats minimum 10 resources, the system shall be able to multiply the cell before it will be hungry again.
8. The system shall be able to multiply the asexual cells by division, resulting two hungry cells.
9. The system shall be able to multiply the sexual cells when they find another sexual cell, resulting in a third hungry cell.

# Non-functional requirements
1. The system shall be stable, the user can not insert out of set range parameters. (As an example T_full and T_starve positive numbers between 2 and 10s)

# High-level component design of the application
![image](https://user-images.githubusercontent.com/80709747/205849651-034dd625-d9b4-4fbf-a39b-d774d62c6bdf.png) 

# Class Diagram
![diagram](https://user-images.githubusercontent.com/80709747/197619420-6e08924f-60c1-48c6-add9-006bb0d3fc82.png)

