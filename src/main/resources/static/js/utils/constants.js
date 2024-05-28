// regex
const email_pattern = /^\w+@[a-zA-Z_]+?\.[a-zA-Z]{2,3}$/
const password_pattern = /^(?=.*[a-zA-Z])(?=.*\d).{4,50}$/
const fullName_pattern = /^.{2,30}$/

// lengths
const min_content_letters_count = 100
const max_content_letters_count = 10000
const min_title_letters_count = 2
const max_title_letters_count = 100
const min_hashtag_letters_count = 2
const max_hashtag_letters_count = 500
const MAX_LENGTH_OF_API_ERROR_MESSAGE = 100
