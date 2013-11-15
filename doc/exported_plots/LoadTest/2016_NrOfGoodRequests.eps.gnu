set term postscript eps color blacktext "Helvetica" 24
set title "Load Test - increase # of clients by 20 every 2 min, ID: 2016"
set xlabel "Time in minutes after test start"
set ylabel "# of successfull Requests in 1000"
set size 2,2
set style line 1 lc rgb '#0060ad' lt 1 lw 2 pt 7 ps 1.5
set style line 2 lc rgb '#dd181f' lt 2 lw 2 pt 5 ps 1.5
set style line 3 lc rgb 'green' lt 3 lw 2 pt 3 ps 1.5
set style line 4 lc rgb '#cccccc' lt 4 lw 2 pt 5 ps 1.5
set style line 5 lc rgb 'black' lt 5 lw 2 pt 5 ps 1.5
plot "-" with errorlines title "# Successfull Requests" ls 1
0 32.487
1 58.307
2 70.257
3 81.096
4 75.32
5 78.261
6 93.222
7 81.171
8 92.599
9 94.34
10 83.39
11 86.027
12 91.231
13 86.742
14 97.766
15 96.891
16 84.647
17 95.456
18 96.885
19 88.633
20 101.202
21 98.655
22 86.859
23 100.888
24 96.574
25 94.565
26 100.583
27 93.535
28 91.216
29 101.302
30 96.194
31 98.432
32 99.97
33 93.053
34 95.822
35 100.779
36 93.295
37 95.912
38 101.812
39 92.812
40 97.896
41 101.66
42 93.123
43 100.923
44 99.622
45 10.002
e
quit
