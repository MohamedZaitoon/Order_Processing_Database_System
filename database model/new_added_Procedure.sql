USE `book_store`;
DROP procedure IF EXISTS `update_book`;

DELIMITER $$
USE `book_store`$$
CREATE PROCEDURE `update_book`(
				in _old_ISBN varchar(13),
				in bISBN varchar(13),
				in bTitle varchar(80) ,
				in bCategory varchar(15), 
				in bSelling_price int,
				in bno_copies int,
				in bthreshold int, 
				in bpublisher varchar(60) ,
				in bpyear date
)
BEGIN
	update book set ISBN = bISBN, title = bTitle, category = bCategory,
					selling_price = bSelling_price, no_copies = bno_copies,
                    threshold = bthreshold, publisher = bpublisher,
                    pyear =bpyear where ISBN = _old_ISBN; 
END$$

DELIMITER ;

