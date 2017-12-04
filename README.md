# Three-dimensional-global-routing
A Fuzzified Global Routing Solution for 3D Integrated Circuits 

This is a global routing tool for VLSI Physical Design for 3 Dimensional Integrated Circuits. 

Please refer to the following paper for more details. 

"Debashri Roy, Prasun Ghosal, Saraju P. Mohanty, FuzzRoute: A Thermally Efficient Congestion Free Global Routing Method for Three Dimensional Integrated Circuits , ACM Transactions on Design Automation of Electronic Systems (TODAES), November 2015."


Instructions to run:
1. Export or clone the repo in the eclipse distribution.
2. Update the jar paths from 'jars' folder in Properties-> Java Build Path
3. Run "Main.java" 
4. One UI would appear. Input the following files: i) Enter the .net file (*.net) ii) Enter the placement file (*.pl) iii) Enter the sensitivity info file (*.info): Refer to the paper for more details. iv) enter the global routing file (*.gr): Please refer to ISPD 1998 and 2008 websites for benchmarks 
5. One output file with global routing solution will be generated. 
