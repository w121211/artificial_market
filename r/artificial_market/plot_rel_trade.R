source("~/git/artificial_market/r/artificial_market/global.R")
f1 = "artificial_market/exp/exp_02.txt"
f2 = "artificial_market/exp/exp_04.txt"
folder_set <- c(f1, f2)

# read table
d <- data.frame()
for (i in 1:length(folder_set)) {
  x <- read.table(folder_set[i], na.strings="NA", header=TRUE, sep="\t")
  d <- rbind(d, x)
}

s <- subset(d, type=="SMM" | d$type=="ST")
p <- subset(d, type=="PMM" | d$type=="ST")

r.trd_ll <- lm(s$p_sd ~ s$trd_ll)
r.trd_ll <- lm(p$p_sd ~ p$trd_ll)