BuildWithbibtex:
	pdflatex Compiler
	bibtex  Compiler
	bibtex  Compiler
	pdflatex  Compiler
	pdflatex  Compiler
	evince Compiler.pdf &
	make CG
	cd ..

CG:
	git add --all
	git commit -m "Progressing CLB IEE_ACCESS submission paper"
	git push

clean:
	rm -rf Compiler.aux
	rm -rf Compiler.bbl
	rm -rf Compiler.log

