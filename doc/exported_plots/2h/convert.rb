%x{ls ./*.gnu}.split(' ').compact.each do |v|
  s = "gnuplot #{v} > #{v.gsub('.gnu', '')}"
  puts s
  gets
  %x{#{s}}
end
