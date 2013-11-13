set term postscript eps color blacktext "Helvetica" 24
set title "Response Time for 2h Test (Client vs. Middleware vs. Database), With 4 Minutes Warmup/Cooldown time, ID: 174"
set xlabel "Time after Measurement Start [m]"
set ylabel "Response Time [ms]"
set size 2,2
set style line 1 lc rgb '#0060ad' lt 1 lw 2 pt 7 ps 1.5
set style line 2 lc rgb '#dd181f' lt 2 lw 2 pt 5 ps 1.5
set style line 3 lc rgb 'green' lt 3 lw 2 pt 3 ps 1.5
set style line 4 lc rgb '#cccccc' lt 4 lw 2 pt 5 ps 1.5
set style line 5 lc rgb 'black' lt 5 lw 2 pt 5 ps 1.5
plot "-" with errorlines title "Client Response Time" ls 1, "-" with errorlines title "Middleware Response Time" ls 2, "-" with errorlines title "Database Response Time" ls 3
0 22.0 7.0 83.0
1 23.0 8.0 79.0
2 24.0 11.0 79.0
3 23.0 10.0 77.0
4 23.0 10.0 82.0
5 23.0 10.0 79.0
6 23.0 9.0 75.0
7 24.0 9.0 73.0
8 24.0 10.0 61.0
9 24.0 12.0 60.0
10 23.0 10.0 60.0
11 24.0 9.0 75.0
12 25.0 10.0 79.0
13 24.0 10.0 66.0
14 24.0 11.0 66.0
15 24.0 12.0 73.0
16 24.0 12.0 73.0
17 23.0 10.0 74.0
18 24.0 11.0 90.0
19 24.0 11.0 83.0
20 23.0 10.0 79.0
21 23.0 11.0 77.0
22 23.0 11.0 71.0
23 24.0 10.0 80.0
24 23.0 9.0 80.0
25 24.0 12.0 73.0
26 24.0 10.0 86.0
27 24.0 10.0 84.0
28 26.0 12.0 72.0
29 24.0 12.0 75.0
30 24.0 11.0 71.0
31 23.0 10.0 67.0
32 23.0 10.0 68.0
33 24.0 10.0 72.0
34 25.0 11.0 79.0
35 23.0 9.0 83.0
36 24.0 11.0 74.0
37 24.0 12.0 63.0
38 24.0 12.0 62.0
39 25.0 10.0 75.0
40 24.0 11.0 77.0
41 23.0 10.0 89.0
42 24.0 9.0 83.0
43 23.0 12.0 76.0
44 25.0 12.0 80.0
45 23.0 10.0 75.0
46 23.0 10.0 71.0
47 24.0 11.0 71.0
48 23.0 10.0 77.0
49 24.0 10.0 75.0
50 25.0 11.0 65.0
51 24.0 11.0 63.0
52 24.0 9.0 70.0
53 24.0 10.0 71.0
54 24.0 11.0 68.0
55 25.0 10.0 81.0
56 24.0 11.0 85.0
57 23.0 11.0 78.0
58 23.0 10.0 79.0
59 23.0 11.0 74.0
60 23.0 11.0 73.0
61 24.0 11.0 77.0
62 24.0 10.0 74.0
63 24.0 10.0 82.0
64 24.0 10.0 89.0
65 24.0 10.0 71.0
66 24.0 11.0 71.0
67 24.0 11.0 61.0
68 24.0 11.0 60.0
69 24.0 11.0 60.0
70 25.0 10.0 76.0
71 25.0 10.0 75.0
72 23.0 10.0 66.0
73 23.0 11.0 63.0
74 24.0 11.0 71.0
75 24.0 10.0 80.0
76 24.0 11.0 78.0
77 24.0 10.0 81.0
78 23.0 10.0 83.0
79 23.0 10.0 78.0
80 25.0 12.0 75.0
81 24.0 11.0 75.0
82 24.0 9.0 82.0
83 24.0 11.0 74.0
84 23.0 10.0 71.0
85 24.0 10.0 86.0
86 25.0 11.0 84.0
87 24.0 11.0 81.0
88 24.0 10.0 75.0
89 24.0 12.0 72.0
90 24.0 12.0 69.0
91 24.0 11.0 72.0
92 25.0 12.0 75.0
93 26.0 10.0 81.0
94 24.0 10.0 72.0
95 24.0 9.0 77.0
96 24.0 12.0 79.0
97 23.0 11.0 72.0
98 25.0 12.0 74.0
99 24.0 11.0 75.0
100 25.0 11.0 74.0
101 25.0 9.0 81.0
102 25.0 11.0 75.0
103 24.0 11.0 77.0
104 26.0 12.0 75.0
105 26.0 11.0 80.0
106 25.0 12.0 78.0
107 25.0 10.0 85.0
108 24.0 9.0 77.0
109 24.0 11.0 75.0
110 25.0 12.0 72.0
111 24.0 11.0 71.0
112 23.0 11.0 59.0
e
0 19.0 6.0 79.0
1 20.0 7.0 76.0
2 22.0 10.0 77.0
3 21.0 9.0 74.0
4 21.0 8.0 79.0
5 21.0 9.0 74.0
6 21.0 8.0 72.0
7 22.0 8.0 71.0
8 22.0 9.0 59.0
9 23.0 11.0 59.0
10 22.0 9.0 58.0
11 21.0 8.0 73.0
12 23.0 9.0 77.0
13 22.0 9.0 64.0
14 22.0 9.0 65.0
15 23.0 11.0 71.0
16 23.0 11.0 72.0
17 21.0 8.0 72.0
18 23.0 9.0 86.0
19 22.0 9.0 81.0
20 21.0 8.0 78.0
21 22.0 10.0 74.0
22 22.0 10.0 69.0
23 22.0 9.0 76.0
24 21.0 8.0 76.0
25 23.0 11.0 72.0
26 22.0 8.0 82.0
27 22.0 9.0 81.0
28 24.0 11.0 70.0
29 23.0 10.0 74.0
30 23.0 10.0 67.0
31 22.0 9.0 62.0
32 22.0 9.0 65.0
33 22.0 9.0 70.0
34 24.0 10.0 78.0
35 21.0 8.0 79.0
36 22.0 10.0 73.0
37 23.0 11.0 62.0
38 23.0 11.0 61.0
39 22.0 9.0 73.0
40 22.0 10.0 75.0
41 21.0 9.0 85.0
42 21.0 8.0 80.0
43 22.0 10.0 74.0
44 23.0 11.0 78.0
45 22.0 9.0 74.0
46 21.0 9.0 70.0
47 22.0 10.0 70.0
48 21.0 8.0 74.07499999999709
49 22.0 9.0 73.0
50 23.0 10.0 64.0
51 22.0 10.0 61.0
52 22.0 8.0 67.0
53 22.0 9.0 69.0
54 22.0 10.0 66.0
55 23.0 9.0 79.0
56 22.0 9.0 81.0
57 22.0 10.0 76.0
58 21.0 8.0 78.0
59 22.0 10.0 72.0
60 22.0 10.0 71.0
61 22.0 10.0 76.0
62 22.0 9.0 72.0
63 22.0 9.0 80.0
64 22.0 9.0 87.0
65 22.0 9.0 69.0
66 23.0 10.0 68.0
67 23.0 10.0 59.0
68 22.0 9.0 58.0
69 22.0 10.0 58.0
70 23.0 9.0 74.0
71 22.0 8.0 73.0
72 22.0 9.0 64.0
73 22.0 9.0 58.0
74 22.0 10.0 69.0
75 22.0 9.0 78.0
76 22.0 10.0 76.0
77 22.0 9.0 79.0
78 22.0 9.0 79.0
79 22.0 9.0 75.0
80 24.0 11.0 73.0
81 22.0 9.0 74.0
82 22.0 8.0 78.0
83 22.0 10.0 73.0
84 21.0 8.0 69.0
85 22.0 9.0 82.0
86 23.0 10.0 82.0
87 23.0 10.0 78.0
88 22.0 9.0 73.0
89 23.0 10.0 71.0
90 23.0 11.0 68.0
91 22.0 10.0 69.0
92 23.0 10.0 73.0
93 23.0 9.0 79.0
94 22.0 9.0 70.0
95 21.0 7.0 75.0
96 23.0 10.0 77.0
97 21.0 9.0 70.0
98 23.0 11.0 73.0
99 23.0 10.0 74.0
100 23.0 10.0 72.0
101 22.0 8.0 79.0
102 23.0 9.0 73.0
103 23.0 9.0 75.0
104 24.0 11.0 73.0
105 24.0 10.0 78.0
106 24.0 11.0 77.0
107 23.0 9.0 83.0
108 21.0 7.0 76.0
109 23.0 10.0 73.0
110 23.0 11.0 70.0
111 23.0 10.0 70.0
112 22.0 10.0 57.0
e
0 9.0 2.0 82.0
1 10.0 2.0 76.0
2 11.0 2.0 78.0
3 10.0 2.0 74.0
4 9.0 2.0 82.0
5 11.0 2.0 76.0
6 10.0 2.0 72.0
7 11.0 2.0 69.0
8 11.0 2.0 51.0
9 13.0 2.0 50.0
10 11.0 2.0 50.0
11 11.0 2.0 70.0
12 16.0 2.0 78.0
13 11.0 2.0 60.0
14 11.0 2.0 61.0
15 11.0 2.0 71.0
16 11.0 2.0 72.0
17 11.0 2.0 72.72499999999854
18 12.0 2.0 94.0
19 10.0 2.0 86.0
20 10.0 2.0 78.0
21 10.0 2.0 74.0
22 11.0 2.0 68.0
23 12.0 2.0 79.0
24 11.0 2.0 78.0
25 11.0 2.0 71.0
26 10.0 2.0 86.0
27 10.0 2.0 82.0
28 14.0 2.0 65.0
29 11.0 2.0 72.0
30 11.0 2.0 64.0
31 11.0 2.0 56.0
32 11.0 2.0 60.0
33 10.0 2.0 66.0
34 12.0 2.0 79.0
35 11.0 2.0 85.0
36 11.0 2.0 72.0
37 13.0 2.0 58.0
38 13.0 2.0 53.0
39 12.0 2.0 73.0
40 10.0 2.0 76.0
41 10.0 2.0 99.0
42 10.0 2.0 83.0
43 11.0 2.0 74.0
44 12.0 2.0 79.0
45 11.0 2.0 75.0
46 10.0 2.0 68.0
47 11.0 2.0 66.0
48 10.0 2.0 76.0
49 11.0 2.0 70.0
50 16.0 2.0 56.0
51 16.0 2.0 53.0
52 15.0 2.0 63.0
53 14.0 2.0 65.0
54 11.0 2.0 68.0
55 11.0 2.0 81.0
56 10.0 2.0 86.47499999999854
57 11.0 2.0 76.0
58 10.0 2.0 79.0
59 11.0 2.0 71.0
60 11.0 2.0 69.0
61 11.0 2.0 75.0
62 11.0 2.0 72.0
63 11.0 2.0 84.0
64 10.0 2.0 102.0
65 12.0 2.0 68.0
66 12.0 2.0 65.0
67 12.0 2.0 50.0
68 12.0 2.0 50.650000000001455
69 16.0 2.0 51.0
70 13.0 2.0 74.0
71 11.0 2.0 69.0
72 11.0 2.0 60.0
73 12.0 2.0 50.0
74 12.0 2.0 70.0
75 11.0 2.0 80.0
76 13.0 2.0 77.0
77 11.0 2.0 80.0
78 10.0 2.0 82.0
79 11.0 2.0 76.0
80 12.0 2.0 70.0
81 11.0 2.0 74.0
82 14.0 2.0 78.0
83 11.0 2.0 71.0
84 11.0 2.0 67.0
85 10.0 2.0 89.0
86 10.0 2.0 86.0
87 12.0 2.0 79.0
88 13.0 2.0 73.0
89 11.0 2.0 70.0
90 11.0 2.0 65.0
91 11.0 2.0 68.0
92 11.0 2.0 71.0
93 11.0 2.0 79.0
94 14.0 2.0 66.0
95 10.0 2.0 75.0
96 11.0 2.0 79.0
97 11.0 2.0 69.0
98 12.0 2.0 70.0
99 11.0 2.0 71.0
100 14.0 2.0 68.0
101 10.0 2.0 81.0
102 11.0 2.0 71.0
103 11.0 2.0 73.0
104 12.0 2.0 70.0
105 12.0 2.0 76.0
106 13.0 2.0 77.0
107 11.0 2.0 88.0
108 10.0 2.0 76.0
109 12.0 2.0 70.0
110 11.0 2.0 67.0
111 13.0 2.0 66.0
112 10.0 2.0 50.0
e
quit
