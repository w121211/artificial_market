source("~/git/artificial_market/r/artificial_market/global.R")
f1 = paste(ROOT, "exp_01_correlation_e1.txt", sep="")
folder_set <- c(f1)

postscript(file="artificial_market/fig/observers_vs_lambda.eps", onefile=FALSE, horizontal=FALSE, width=8, height=8, paper="special")
#png(file="artificial_market/fig/observers_vs_lambda.png", width=400, height=400)

# load data
d <- read.table(f1, na.strings="NA", header=TRUE, sep="\t")
s <- subset(d, type=="SMM" | d$type=="ST")
smm <- subset(d, type=="SMM")
p <- subset(d, type=="PMM" | d$type=="ST")
pmm <- subset(d, type=="PMM")

# plot settings
par(mfrow=c(3,2))          # (rows, cols)
#par(mar=c(5.1, 4.1, 4.1, 8.1), xpd=TRUE)

# plot function
myplot <- function(x, y1, y2, xlab='x', ylab='y', title='') {
  plot(range(x), range(y1, y2), type="n", xlab=xlab, ylab=ylab)
  lines(x, y1, type="o", col="red")
  lines(x, y2, type="o", col="blue")
  grid()
  title(title)
  #legend("topright", col=c("red","blue"), lty=c(1,2), legend=c("p_t","p_f_t"), cex=.6)
  #legend("topright", inset=c(-0.4,0), legend=c("SMM","PMM"), lty=c(1,1), cex=.8)
}

myplot2 <- function(x, y1, y2, xlab='x', ylab='y', title='') {
  plot(range(x), range(y1, y2), type="n", xlab=xlab, ylab=ylab)
  lines(x, y1, type="p", col="red")
  lines(x, y2, type="p", col="blue")
  grid()
  title(title)
  #legend("topright", col=c("red","blue"), lty=c(1,2), legend=c("p_t","p_f_t"), cex=.6)
  #legend("topright", inset=c(-0.4,0), legend=c("SMM","PMM"), lty=c(1,1), cex=.8)
  
  # fit a loess line
  df <- data.frame(x, y1)
  df.loess <- loess(y1 ~ x, span=1.4)
  df.predict <- predict(df.loess)
  lines(x[order(x)], df.predict[order(x)], col="red", lwd=1)
  
  df <- data.frame(x, y2)
  df.loess <- loess(y2 ~ x, span=1.4)
  df.predict <- predict(df.loess)
  lines(x[order(x)], df.predict[order(x)], col="blue", lwd=1)
  #scater.smooth(x, y1)
}

# plot observations vs. lambda
if (F) {
  myplot(title='Std dev. of market prices', s$lambda_mu, s$p_sd, p$p_sd, xlab=expression(lambda), ylab='std dev.')
  myplot(title='Average bid-ask spread', s$lambda_mu, s$spread, p$spread, xlab=expression(lambda), ylab='spread')
  myplot(title='Std dev. of returns', s$lambda_mu, s$r_sd, p$r_sd, xlab=expression(lambda), ylab='std dev.')
  myplot(title='Kurtosis of returns', s$lambda_mu, s$r_kt, p$r_kt, xlab=expression(lambda), ylab='kurtosis')
  myplot(title='Total trade volume', s$lambda_mu, s$trd_sum, p$trd_sum, xlab=expression(lambda), ylab='volume (shares)')
  myplot(title='Average asset of a market maker', smm$lambda_mu, smm$hft_asset_mu, pmm$hft_asset_mu, xlab=expression(lambda), ylab='asset')
}

# plot observations vs. estimator
if (T) {
  #e = expression(len. %*% vol.^2 %*% lambda^2)
  e = expression(e)
  myplot2(title='Std dev. of market prices', s$e_1, s$p_sd, p$p_sd, xlab=e, ylab='std dev.')
  myplot2(title='Average bid-ask spread', s$e_1, s$spread, p$spread, xlab=e, ylab='spread')
  myplot2(title='Std dev. of returns', s$e_1, s$r_sd, p$r_sd, xlab=e, ylab='std dev.')
  myplot2(title='Kurtosis of returns', s$e_1, s$r_kt, p$r_kt, xlab=e, ylab='kurtosis')
  myplot2(title='Total trade volume', s$e_1, s$trd_sum, p$trd_sum, xlab=e, ylab='volume (shares)')
  myplot2(title='Average asset of a market maker', smm$e_1, smm$hft_asset_mu, pmm$hft_asset_mu, xlab=e, ylab='asset')
}

dev.off()