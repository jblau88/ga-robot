# ga-robot
@author Joshua Blau
@version 1.0

This program is designed to experiment with a fairly simplistic genetic algorithm scenario, based on design details from the book "Complexity: A Guided Tour" by Melanie Mitchell.

The program creates a population of robots with random instruction sets to perform in certain scenarios within a larger environment, upon which they are evaluated for fitness and then pass portions of these instruction sets onto a new generation of robots based on fitness scores. As Mitchell explains in her book, such a program will eventually lead to the creation of robots with close to ideal instruction sets, given the allowance of a proper system of evolution via competition. The intent of the program is to experiment with how changing various factors changes the overall result, including the rate of evolution and the final performance of most ideal robot(s), as well as other factors.

As of the current version, the program does not achieve Mitchell's final result. The next version will attempt to fix this problem, as well as improving the user interface and perhaps beginning construction for a basic graphical representation.
