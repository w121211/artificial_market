source("~/git/artificial_market/r/artificial_market/global.R")
f1 = paste(ROOT, "exp_01_correlation.txt", sep="")
folder_set <- c(f1)

# load data
d <- read.table(f1, na.strings="NA", header=TRUE, sep="\t")
ss <- subset(d, type=="SMM" | d$type=="ST")
smm <- subset(d, type=="SMM")
sp <- subset(d, type=="PMM" | d$type=="ST")
pmm <- subset(d, type=="PMM")

d <- subset(d, select = -c(type))
d.cor <- cor(d)

ss <- subset(ss, select = -c(type))
ss.cor <- cor(ss)

sp <- subset(sp, select = -c(type))
sp.cor <- cor(sp)