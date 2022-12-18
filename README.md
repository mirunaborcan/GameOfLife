# GameOfLife
The goal of this application is to simulate a cells population able to reproduce and eat. 

# Functional requirements 
1. The system shall be able to set the number of sexual and asexual cells.
2. The system shall be able to set the number of food resources.
3 The system shall be able to set the period of time in which a cell is not hungry (timeFullInitial).
4. The system shall be able to add the period of time in which a cell is hungry (timeStarveInitial).
5. The system shall be able to kill a cell if it isn't fed in timeStarveInitial period.
6. The system shall be able to create 1-5 new food resources after a cell dies.
7. After a cell eats minimum 10 resources, the system shall be able to multiply the cell before it will be hungry again.
8. The system shall be able to multiply the asexual cells by division, resulting two hungry cells.
9. The system shall be able to multiply the sexual cells when they find another sexual cell, resulting in a third hungry cell.

# Non-functional requirements
1. The system shall be stable.

# High-level component design of the application
 ![diag1 drawio](https://user-images.githubusercontent.com/80709747/208319526-cfd317a5-72f3-429b-a3ca-981c539cca34.png)

# Class Diagram
![classdiag drawio](https://user-images.githubusercontent.com/80709747/208321045-f38f8ba1-f011-445f-9252-e40c637b4e9a.png)
